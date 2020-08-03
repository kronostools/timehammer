package com.kronostools.timehammer.core.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.ScheduleTriggerMessage;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorker;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorkerBuilder;
import com.kronostools.timehammer.common.messages.schedules.model.WorkerCurrentPreferences;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.core.dao.WorkerCurrentPreferencesDao;
import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Stream;

@ApplicationScoped
public class UpdateWorkersHolidayScheduleProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateWorkersHolidayScheduleProcessor.class);

    private final WorkerCurrentPreferencesDao workerCurrentPreferencesDao;

    public UpdateWorkersHolidayScheduleProcessor(final WorkerCurrentPreferencesDao workerCurrentPreferencesDao) {
        this.workerCurrentPreferencesDao = workerCurrentPreferencesDao;
    }

    @Incoming(Channels.SCHEDULE_UPDATE_HOLIDAYS)
    @Outgoing(Channels.HOLIDAYS_WORKER_GET)
    public Multi<Message<UpdateWorkersHolidayWorker>> process(final Message<ScheduleTriggerMessage> message) {
        final ScheduleTriggerMessage triggerMessage = message.getPayload();

        LOG.info("Received trigger message to run schedule '{}' with timestamp '{}'", triggerMessage.getName(), CommonDateTimeUtils.formatDateTimeToLog(triggerMessage.getGenerated()));

        /*
        final List<UpdateWorkersHolidayWorker> workers = new ArrayList<>();

        workerCurrentPreferencesDao.findAll(triggerMessage.getGenerated().toLocalDate())
            .onItem().invoke(workerCurrentPreferencesMultipleResult -> {
                if (workerCurrentPreferencesMultipleResult.isSuccessful()) {
                    LOG.debug("Transforming result from db to list of workers ...");

                    final List<WorkerCurrentPreferences> wcpl = workerCurrentPreferencesMultipleResult.getResult();

                    wcpl.forEach(wcp -> workers
                            .add(new UpdateWorkersHolidayWorkerBuilder()
                                    .generated(triggerMessage.getGenerated())
                                    .executionId(triggerMessage.getExecutionId())
                                    .name(triggerMessage.getName())
                                    .batchSize(wcpl.size())
                                    .workerInternalId(wcp.getWorkerInternalId())
                                    .company(wcp.getCompany())
                                    .workerExternalId(wcp.getWorkerExternalId())
                                    .holidayCandidate(triggerMessage.getGenerated().toLocalDate())
                                    .build()));
                } else {
                    LOG.error("Holidays of workers will not be updated because there was an unexpected error while recovering list of workers. Error: {}", workerCurrentPreferencesMultipleResult.getErrorMessage());
                }
            })
            .await().indefinitely();
        */

        return workerCurrentPreferencesDao.findAll(triggerMessage.getGenerated().toLocalDate())
                .onItem().produceMulti(workerCurrentPreferencesMultipleResult -> {
                    if (workerCurrentPreferencesMultipleResult.isSuccessful()) {
                        LOG.debug("Transforming result from db to list of workers ...");

                        final List<WorkerCurrentPreferences> wcpl = workerCurrentPreferencesMultipleResult.getResult();

                        LOG.debug("Holidays of {} workers will be updated", wcpl.size());

                        return Multi.createFrom().items(wcpl.stream()
                                .map(wcp -> new UpdateWorkersHolidayWorkerBuilder()
                                        .generated(triggerMessage.getGenerated())
                                        .executionId(triggerMessage.getExecutionId())
                                        .name(triggerMessage.getName())
                                        .batchSize(wcpl.size())
                                        .workerInternalId(wcp.getWorkerInternalId())
                                        .company(wcp.getCompany())
                                        .workerExternalId(wcp.getWorkerExternalId())
                                        .holidayCandidate(triggerMessage.getGenerated().toLocalDate())
                                        .build()))
                                .map(Message::of);
                    } else {
                        LOG.error("Status of workers will not be updated because there was an unexpected error while recovering list of workers. Error: {}", workerCurrentPreferencesMultipleResult.getErrorMessage());

                        return Multi.createFrom().items(Stream.empty());
                    }
                }).on().completion(() -> {
                    LOG.debug("Acknowleding trigger message of schedule '{}' ...", triggerMessage.getName());
                    message.ack();
                    LOG.debug("Acknowleded trigger message of schedule '{}'", triggerMessage.getName());
                });

        /*
        LOG.debug("Holidays of {} workers will be updated", workers.size());

        return Multi.createFrom().iterable(workers.stream().map(Message::of).collect(Collectors.toList()))
                .onCompletion().invoke(() -> {
                    LOG.debug("Acknowleding trigger message of schedule '{}' ...", triggerMessage.getName());
                    message.ack();
                    LOG.debug("Acknowleded trigger message of schedule '{}'", triggerMessage.getName());
                });
        */
    }
}