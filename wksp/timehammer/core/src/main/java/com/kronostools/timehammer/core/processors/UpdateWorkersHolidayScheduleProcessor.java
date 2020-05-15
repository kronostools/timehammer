package com.kronostools.timehammer.core.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.ScheduleTriggerMessage;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorker;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorkerBuilder;
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
import java.util.stream.Collectors;

@ApplicationScoped
public class UpdateWorkersHolidayScheduleProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateWorkersHolidayScheduleProcessor.class);

    private final WorkerCurrentPreferencesDao workerCurrentPreferencesDao;

    public UpdateWorkersHolidayScheduleProcessor(final WorkerCurrentPreferencesDao workerCurrentPreferencesDao) {
        this.workerCurrentPreferencesDao = workerCurrentPreferencesDao;
    }

    @Incoming(Channels.SCHEDULE_UPDATE_HOLIDAYS)
    @Outgoing(Channels.HOLIDAYS_WORKER_AUTH)
    public Multi<Message<UpdateWorkersHolidayWorker>> process(final Message<ScheduleTriggerMessage> message) {
        final ScheduleTriggerMessage triggerMessage = message.getPayload();

        LOG.info("Received trigger message to run schedule '{}' with timestamp '{}'", triggerMessage.getName(), CommonDateTimeUtils.formatDateTimeToLog(triggerMessage.getTimestamp()));

        final List<Message<UpdateWorkersHolidayWorker>> workers = workerCurrentPreferencesDao.findAll(triggerMessage.getTimestamp())
                .map(wcpl -> {
                    LOG.debug("Transforming result from db to list of workers ...");

                    return wcpl.stream()
                            .map(wcp -> Message.of(new UpdateWorkersHolidayWorkerBuilder()
                                    .timestamp(triggerMessage.getTimestamp())
                                    .executionId(triggerMessage.getExecutionId())
                                    .name(triggerMessage.getName())
                                    .batchSize(wcpl.size())
                                    .workerInternalId(wcp.getWorkerInternalId())
                                    .company(wcp.getCompany())
                                    .workerExternalId(wcp.getWorkerExternalId())
                                    .holidayCandidate(triggerMessage.getTimestamp().toLocalDate())
                                    .build()))
                            .collect(Collectors.toList());
                })
                .await().indefinitely();

        LOG.debug("Holidays of {} workers will be updated", workers.size());

        return Multi.createFrom().iterable(workers)
                .onCompletion().invoke(() -> {
                    LOG.debug("Acknowleding trigger message ...");
                    message.ack();
                    LOG.debug("Acknowleded trigger message");
                });
    }
}