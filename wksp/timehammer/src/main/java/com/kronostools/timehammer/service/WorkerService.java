package com.kronostools.timehammer.service;

import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.dto.ComunytekHolidaysDto;
import com.kronostools.timehammer.comunytek.dto.ComunytekStatusDto;
import com.kronostools.timehammer.comunytek.enums.ComunytekStatusValue;
import com.kronostools.timehammer.config.TimehammerConfig;
import com.kronostools.timehammer.dao.SsidTrackingEventDao;
import com.kronostools.timehammer.enums.WorkerStatusEventType;
import com.kronostools.timehammer.manager.*;
import com.kronostools.timehammer.utils.Constants;
import com.kronostools.timehammer.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class WorkerService {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerService.class);

    private final TimehammerConfig timehammerConfig;
    private final SsidTrackingEventDao ssidTrackingEventDao;
    private final WorkerManager workerManager;
    private final WorkerHolidayManager workerHolidayManager;
    private final WorkerChatManager workerChatManager;
    private final CityHolidayManager cityHolidayManager;
    private final WorkerPreferencesManager workerPreferencesManager;
    private final WorkerStatusService workerStatusService;
    private final ComunytekClient comunytekClient;

    private AtomicLong workerStatusAvgProcessingTime = new AtomicLong(Constants.WORKER_STATUS_INITIAL_AVG_PROCESSING_TIME);

    public WorkerService(final TimehammerConfig timehammerConfig,
                         final SsidTrackingEventDao ssidTrackingEventDao,
                         final WorkerManager workerManager,
                         final WorkerHolidayManager workerHolidayManager,
                         final WorkerChatManager workerChatManager,
                         final CityHolidayManager cityHolidayManager,
                         final WorkerPreferencesManager workerPreferencesManager,
                         final WorkerStatusService workerStatusService,
                         final ComunytekClient comunytekClient) {
        this.timehammerConfig = timehammerConfig;
        this.ssidTrackingEventDao = ssidTrackingEventDao;
        this.workerManager = workerManager;
        this.workerHolidayManager = workerHolidayManager;
        this.workerChatManager = workerChatManager;
        this.cityHolidayManager = cityHolidayManager;
        this.workerPreferencesManager = workerPreferencesManager;
        this.workerStatusService = workerStatusService;
        this.comunytekClient = comunytekClient;
    }

    @Transactional
    public Optional<WorkerVo> getWorkerByChatId(final String chatId) {
        return workerManager.getWorkerByChatId(chatId);
    }

    @Transactional
    public void removeChat(final String chatId) {
        getWorkerByChatId(chatId).ifPresent(workerVo -> workerChatManager.removeChat(workerVo.getExternalId(), chatId));
    }

    public WorkerAndPreferencesVo getWorkerAndPreferencesByExternalId(final String externalId) {
        WorkerVo workerVo = workerManager.getWorkerByExternalId(externalId);
        WorkerPreferencesVo preferencesVo = workerPreferencesManager.getWorkerPreferences(externalId);

        return new WorkerAndPreferencesVo(workerVo, preferencesVo);
    }

    public WorkerNonWorkingDaysVo getNonWorkingDays(final String externalId) {
        WorkerPreferencesVo workerPreferencesVo = workerPreferencesManager.getWorkerPreferences(externalId);

        Set<LocalDate> workerHolidays = workerHolidayManager.getPendingWorkerHolidays(externalId);
        Set<LocalDate> cityHolidays = cityHolidayManager.getPendingCityHolidays(workerPreferencesVo.getCityCode());

        return new WorkerNonWorkingDaysVo(externalId, workerHolidays, cityHolidays);
    }

    @Transactional
    public void registerWorker(final WorkerVo workerVo, final WorkerPreferencesVo workerPreferencesVo, final WorkerChatVo workerChatVo) {
        workerManager.registerWorker(workerVo, workerPreferencesVo, workerChatVo);
    }

    public List<WorkerVo> getAllWorkers() {
        return workerManager.getAllWorkers();
    }

    public void updateWorkersHolidays() {
        workerManager.getAllWorkers().forEach(worker -> {
            try {
                this.updateWorkerHolidays(worker);
                LOG.info("Holidays of worker '{}' updated successfully", worker.getExternalId());
            } catch (Exception e) {
                LOG.warn("Holidays of worker '{}' could NOT be updated. Error: {}", worker.getExternalId(), e.getMessage());
            }
        });
    }

    @Transactional
    public void updateWorkerHolidays(final WorkerVo workerCredentialsDto) {
        LOG.debug("BEGIN updateWorkerHolidays: [{}]", workerCredentialsDto);

        ComunytekHolidaysDto comunytekHolidaysDto = comunytekClient.getHolidays(workerCredentialsDto.getExternalId(), workerCredentialsDto.getExternalPassword());

        workerHolidayManager.updateWorkerHolidays(workerCredentialsDto.getExternalId(), comunytekHolidaysDto);

        LOG.debug("END updateWorkerHolidays");
    }

    public void updateWorkersStatus(final LocalDateTime timestamp) {
        final long numberOfWorkersToProcess = Double.valueOf(timehammerConfig.getSchedules().getUpdateWorkersStatus().getIntervalInMillis() * 0.80 / workerStatusAvgProcessingTime.get()).intValue();

        AtomicInteger workersProcessedSuccessfully = new AtomicInteger(0);
        AtomicInteger workersProcessedWithError = new AtomicInteger(0);
        AtomicLong workersTotalProcessingTime = new AtomicLong(0L);

        final List<WorkerCurrentPreferencesVo> workersToProcess = workerPreferencesManager.getAllWorkersPreferences()
                .stream()
                .map(wp -> wp.getAt(timestamp))
                .filter(WorkerCurrentPreferencesVo::workToday)
                .collect(Collectors.toList());

        Collections.shuffle(workersToProcess);

        LOG.info("GIVEN: Average worker status update time is {} millis AND Workers status is updated every {} millis -- (THEN) -> Status of {} workers (at maximum) will be updated (current number of workers is {})", workerStatusAvgProcessingTime.get(), timehammerConfig.getSchedules().getUpdateWorkersStatus().getIntervalInMillis(), numberOfWorkersToProcess, workersToProcess.size());

        workersToProcess.stream().limit(numberOfWorkersToProcess).forEach(worker -> {
            try {
                long workerProcessingTime = this.updateWorkerStatus(timestamp, worker);
                LOG.info("Status of worker '{}' updated successfully (processed in {} millis)", worker.getWorkerExternalId(), workerProcessingTime);

                workersProcessedSuccessfully.incrementAndGet();
                workersTotalProcessingTime.addAndGet(workerProcessingTime);
            } catch (Exception e) {
                LOG.warn("Status of worker '{}' could NOT be updated. Error: {}", worker.getWorkerExternalId(), e.getMessage());
                workersProcessedWithError.incrementAndGet();
            }
        });

        LOG.info("Updated status of {} workers (Status of {} workers could not be updated)", workersProcessedSuccessfully.get(), workersProcessedWithError.get());

        if (workersProcessedSuccessfully.get() > 0) {
            workerStatusAvgProcessingTime.set(workersTotalProcessingTime.get() / workersProcessedSuccessfully.get());
        }
    }

    public long updateWorkerStatus(final LocalDateTime timestamp, final WorkerCurrentPreferencesVo workerCurrentPreferences) {
        LOG.debug("BEGIN updateWorkerStatus: [{}]", workerCurrentPreferences);

        final long start = System.currentTimeMillis();

        final WorkerVo workerVo = workerManager.getWorkerByExternalId(workerCurrentPreferences.getWorkerExternalId());
        final ComunytekStatusDto workerCurrentStatus = comunytekClient.getStatus(workerVo.getExternalId(), workerVo.getExternalPassword(), timestamp);

        if (workerCurrentStatus.getStatus() == ComunytekStatusValue.UNKNOWN) {
            LOG.warn("Status of worker could not be updated because the status obtained is UNKNOWN");
        } else {
            if (workerCurrentStatus.getStatus() == ComunytekStatusValue.ENDED) {
                LOG.info("Worker has already ended working, nothing to process");
            } else {
                // TODO: process ssid tracking events
                //List<SsidTrackingEventVo> ssidTrackingEvents = ssidTrackingEventDao.getSsidTrackingEventsBetween(workerStatusLastReviewed, timestamp);

                // TODO: determine worker status event type taking into account the worker ssid tracking events
                WorkerStatusEventType statusEvent = WorkerStatusEventType.TICK;

                workerStatusService.processStatusEvent(timestamp, statusEvent, workerCurrentStatus, workerCurrentPreferences);
            }
        }

        LOG.debug("END updateWorkerStatus");

        return System.currentTimeMillis() - start;
    }
}