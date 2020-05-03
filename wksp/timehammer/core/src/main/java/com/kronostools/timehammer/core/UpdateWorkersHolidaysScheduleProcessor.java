package com.kronostools.timehammer.core;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.ScheduleTriggerMessage;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidaysWorker;
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
public class UpdateWorkersHolidaysScheduleProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateWorkersHolidaysScheduleProcessor.class);

    private final WorkerCurrentPreferencesDao workerCurrentPreferencesDao;

    public UpdateWorkersHolidaysScheduleProcessor(final WorkerCurrentPreferencesDao workerCurrentPreferencesDao) {
        this.workerCurrentPreferencesDao = workerCurrentPreferencesDao;
    }

    @Incoming(Channels.HOLIDAYS_UPDATE)
    @Outgoing(Channels.HOLIDAYS_WORKER_AUTH)
    public Multi<Message<UpdateWorkersHolidaysWorker>> process(final Message<ScheduleTriggerMessage> message) {
        final ScheduleTriggerMessage triggerMessage = message.getPayload();

        LOG.info("Received trigger message to run schedule '{}' with timestamp '{}'", triggerMessage.getName(), CommonDateTimeUtils.formatDateTimeToLog(triggerMessage.getTimestamp()));

        final List<Message<UpdateWorkersHolidaysWorker>> workers = workerCurrentPreferencesDao.findAll(triggerMessage.getTimestamp())
                .map(wcpl -> {
                    LOG.debug("Transforming result from db to list of workers ...");

                    return wcpl.stream()
                            .map(wcp -> Message.of(UpdateWorkersHolidaysWorker.Builder.builder()
                                    .timestamp(triggerMessage.getTimestamp())
                                    .executionId(triggerMessage.getExecutionId())
                                    .name(triggerMessage.getName())
                                    .batchSize(wcpl.size())
                                    .workerInternalId(wcp.getWorkerInternalId())
                                    .workerExternalId(wcp.getWorkerExternalId())
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