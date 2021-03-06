package com.kronostools.timehammer.service;

import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.dto.ComunytekHolidaysDto;
import com.kronostools.timehammer.comunytek.dto.ComunytekStatusDto;
import com.kronostools.timehammer.config.TimehammerConfig;
import com.kronostools.timehammer.enums.WorkerStatusEventType;
import com.kronostools.timehammer.manager.WorkerChatManager;
import com.kronostools.timehammer.manager.WorkerHolidayManager;
import com.kronostools.timehammer.manager.WorkerManager;
import com.kronostools.timehammer.manager.WorkerPreferencesManager;
import com.kronostools.timehammer.utils.Constants;
import com.kronostools.timehammer.vo.WorkerChatVo;
import com.kronostools.timehammer.vo.WorkerCurrentPreferencesVo;
import com.kronostools.timehammer.vo.WorkerPreferencesVo;
import com.kronostools.timehammer.vo.WorkerVo;
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
    private final WorkerManager workerManager;
    private final WorkerHolidayManager workerHolidayManager;
    private final WorkerChatManager workerChatManager;
    private final WorkerPreferencesManager workerPreferencesManager;
    private final WorkerStatusService workerStatusService;
    private final ComunytekClient comunytekClient;
    private final WorkerCredentialsService workerCredentialsService;

    private final AtomicLong workerStatusAvgProcessingTime = new AtomicLong(Constants.WORKER_STATUS_INITIAL_AVG_PROCESSING_TIME);

    public WorkerService(final TimehammerConfig timehammerConfig,
                         final WorkerManager workerManager,
                         final WorkerHolidayManager workerHolidayManager,
                         final WorkerChatManager workerChatManager,
                         final WorkerPreferencesManager workerPreferencesManager,
                         final WorkerStatusService workerStatusService,
                         final ComunytekClient comunytekClient,
                         final WorkerCredentialsService workerCredentialsService) {
        this.timehammerConfig = timehammerConfig;
        this.workerManager = workerManager;
        this.workerHolidayManager = workerHolidayManager;
        this.workerChatManager = workerChatManager;
        this.workerPreferencesManager = workerPreferencesManager;
        this.workerStatusService = workerStatusService;
        this.comunytekClient = comunytekClient;
        this.workerCredentialsService = workerCredentialsService;
    }

    @Transactional
    public Optional<WorkerVo> findWorkerByChatId(final String chatId) {
        return workerManager.findWorkerByChatId(chatId);
    }

    @Transactional
    public void removeChat(final String internalId, final String chatId) {
        workerChatManager.removeChat(internalId, chatId);
    }

    public WorkerPreferencesVo getWorkerPreferencesByInternalId(final String internalId) {
        return workerPreferencesManager.getWorkerPreferencesByInternalId(internalId);
    }

    public WorkerCurrentPreferencesVo getWorkerCurrentPreferencesByInternalId(final String internalId, final LocalDateTime timestamp) {
        return workerPreferencesManager.getWorkerCurrentPreferencesByInternalId(internalId, timestamp);
    }

    public Set<LocalDate> getPendingWorkerHolidays(final String internalId) {
        return workerHolidayManager.getPendingWorkerHolidays(internalId);
    }

    @Transactional
    public void registerWorker(final WorkerVo workerVo, final WorkerPreferencesVo workerPreferencesVo, final WorkerChatVo workerChatVo) {
        workerManager.registerWorker(workerVo, workerPreferencesVo, workerChatVo);
    }

    public List<WorkerVo> getAllWorkers() {
        return workerManager.getAllWorkers();
    }

    public void updateWorkersHolidays(final LocalDateTime timestamp) {
        workerPreferencesManager.getAllWorkersCurrentPreferences(timestamp).forEach(worker -> {
            try {
                this.updateWorkerHolidays(worker);
                LOG.info("Holidays of worker '{}' updated successfully", worker.getWorkerInternalId());
            } catch (Exception e) {
                LOG.warn("Holidays of worker '{}' could NOT be updated. Error: {}", worker.getWorkerInternalId(), e.getMessage());
            }
        });
    }

    @Transactional
    public void updateWorkerHolidays(final WorkerCurrentPreferencesVo workerCurrentPreferences) {
        LOG.debug("BEGIN updateWorkerHolidays: [{}]", workerCurrentPreferences);

        final String externalPassword = workerCredentialsService.getWorkerCredentials(workerCurrentPreferences.getWorkerInternalId());

        if (externalPassword != null) {
            final ComunytekHolidaysDto comunytekHolidaysDto = comunytekClient.getHolidays(workerCurrentPreferences.getWorkerExternalId(), externalPassword);

            workerHolidayManager.updateWorkerHolidays(workerCurrentPreferences.getWorkerInternalId(), comunytekHolidaysDto);
        } else {
            throw new RuntimeException("Missing worker credentials");
        }

        LOG.debug("END updateWorkerHolidays");
    }

    public void updateWorkersStatus(final LocalDateTime timestamp) {
        LOG.debug("BEGIN updateWorkersStatus: [{}]", TimeMachineService.formatDateTimeFull(timestamp));

        final long numberOfWorkersToProcess = Double.valueOf(timehammerConfig.getSchedules().getUpdateWorkersStatus().getIntervalInMillis() * 0.80 / workerStatusAvgProcessingTime.get()).intValue();

        AtomicInteger workersProcessedSuccessfully = new AtomicInteger(0);
        AtomicInteger workersProcessedWithError = new AtomicInteger(0);
        AtomicLong workersTotalProcessingTime = new AtomicLong(0L);

        final List<WorkerCurrentPreferencesVo> workersToProcess = workerPreferencesManager.getAllWorkersCurrentPreferences(timestamp)
                .stream()
                .filter(WorkerCurrentPreferencesVo::workToday)
                .collect(Collectors.toList());

        Collections.shuffle(workersToProcess);

        LOG.info("GIVEN: Average worker status update time is {} millis AND Workers status is updated every {} millis -- (THEN) -> Status of {} workers (at maximum) will be updated (current number of workers is {})", workerStatusAvgProcessingTime.get(), timehammerConfig.getSchedules().getUpdateWorkersStatus().getIntervalInMillis(), numberOfWorkersToProcess, workersToProcess.size());

        workersToProcess.stream().limit(numberOfWorkersToProcess).forEach(worker -> {
            try {
                long workerProcessingTime = this.updateWorkerStatus(timestamp, worker);
                LOG.info("Status of worker '{}' updated successfully (processed in {} millis)", worker.getWorkerInternalId(), workerProcessingTime);

                workersProcessedSuccessfully.incrementAndGet();
                workersTotalProcessingTime.addAndGet(workerProcessingTime);
            } catch (Exception e) {
                LOG.warn("Status of worker '{}' could NOT be updated. Error: {}", worker.getWorkerInternalId(), e.getMessage());
                workersProcessedWithError.incrementAndGet();
            }
        });

        LOG.info("Updated status of {} workers (Status of {} workers could not be updated)", workersProcessedSuccessfully.get(), workersProcessedWithError.get());

        if (workersProcessedSuccessfully.get() > 0) {
            workerStatusAvgProcessingTime.set(workersTotalProcessingTime.get() / workersProcessedSuccessfully.get());
        }

        LOG.debug("END updateWorkersStatus");
    }

    @Transactional
    public long updateWorkerStatus(final LocalDateTime timestamp, final WorkerCurrentPreferencesVo workerCurrentPreferences) {
        LOG.debug("BEGIN updateWorkerStatus: [{}]", workerCurrentPreferences);

        final long start = System.currentTimeMillis();

        final String externalPassword = workerCredentialsService.getWorkerCredentials(workerCurrentPreferences.getWorkerInternalId());

        // TODO: process ssid tracking events
        //List<SsidTrackingEventVo> ssidTrackingEvents = ssidTrackingEventDao.getSsidTrackingEventsBetween(workerStatusLastReviewed, timestamp);

        // TODO: determine worker status event type taking into account the worker ssid tracking events
        WorkerStatusEventType statusEvent = WorkerStatusEventType.TICK;

        workerStatusService.processStatusEvent(timestamp, statusEvent, workerCurrentPreferences, externalPassword);

        LOG.debug("END updateWorkerStatus");

        return System.currentTimeMillis() - start;
    }

    public ComunytekStatusDto getWorkerStatus(final String internalId, final String externalPassword, final LocalDateTime timestamp) {
        final WorkerPreferencesVo workerPreferences = workerPreferencesManager.getWorkerPreferencesByInternalId(internalId);

        return comunytekClient.getStatus(workerPreferences.getWorkerExternalId(), externalPassword, timestamp);
    }

    @Transactional
    public void cleanPastWorkersHolidaysUntil(final LocalDateTime timestamp) {
        workerHolidayManager.cleanPastWorkersHolidaysUntil(timestamp);
    }
}