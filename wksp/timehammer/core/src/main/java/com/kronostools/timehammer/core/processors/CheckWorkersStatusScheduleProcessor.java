package com.kronostools.timehammer.core.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorker;
import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorkerBuilder;
import com.kronostools.timehammer.common.messages.schedules.ScheduleTriggerMessage;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@ApplicationScoped
public class CheckWorkersStatusScheduleProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(CheckWorkersStatusScheduleProcessor.class);

    private final WorkerCurrentPreferencesDao workerCurrentPreferencesDao;

    public CheckWorkersStatusScheduleProcessor(final WorkerCurrentPreferencesDao workerCurrentPreferencesDao) {
        this.workerCurrentPreferencesDao = workerCurrentPreferencesDao;
    }

    @Incoming(Channels.SCHEDULE_UPDATE_STATUS)
    @Outgoing(Channels.STATUS_WORKER_GET)
    public Multi<Message<CheckWorkersStatusWorker>> process(final Message<ScheduleTriggerMessage> message) {
        final ScheduleTriggerMessage triggerMessage = message.getPayload();

        LOG.info("Received trigger message to run schedule '{}' with timestamp '{}'", triggerMessage.getName(), CommonDateTimeUtils.formatDateTimeToLog(triggerMessage.getGenerated()));

        final List<CheckWorkersStatusWorker> workers = new ArrayList<>();

        /*
        workerCurrentPreferencesDao.findAll(triggerMessage.getGenerated().toLocalDate())
            .onItem().invoke(workerCurrentPreferencesMultipleResult -> {
                if (workerCurrentPreferencesMultipleResult.isSuccessful()) {
                    LOG.debug("Transforming result from db to list of workers ...");

                    final List<WorkerCurrentPreferences> wcpl = workerCurrentPreferencesMultipleResult.getResult();

                    wcpl.stream()
                            .filter(WorkerCurrentPreferences::workToday)
                            .forEach(wcp -> workers.add(new CheckWorkersStatusWorkerBuilder()
                                    .generated(triggerMessage.getGenerated())
                                    .executionId(triggerMessage.getExecutionId())
                                    .name(triggerMessage.getName())
                                    .batchSize(wcpl.size())
                                    .workerCurrentPreferences(wcp)
                                    .build()));
                } else {
                    LOG.error("Status of workers will not be updated because there was an unexpected error while recovering list of workers. Error: {}", workerCurrentPreferencesMultipleResult.getErrorMessage());
                }
            })
            .await()
                .indefinitely();
                //.atMost(Duration.ofMillis(1500L));
        */

        return workerCurrentPreferencesDao.findAll(triggerMessage.getGenerated().toLocalDate())
                .onItem().produceMulti(workerCurrentPreferencesMultipleResult -> {
                    if (workerCurrentPreferencesMultipleResult.isSuccessful()) {
                        LOG.debug("Transforming result from db to list of workers ...");

                        final List<WorkerCurrentPreferences> wcpl = workerCurrentPreferencesMultipleResult.getResult();

                        return Multi.createFrom().items(wcpl.stream()
                                .filter(WorkerCurrentPreferences::workToday)
                                .map(wcp -> new CheckWorkersStatusWorkerBuilder()
                                        .generated(triggerMessage.getGenerated())
                                        .executionId(triggerMessage.getExecutionId())
                                        .name(triggerMessage.getName())
                                        .batchSize(wcpl.size())
                                        .workerCurrentPreferences(wcp)
                                        .build())
                                .map(Message::of));
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
        LOG.debug("Status of {} workers will be updated", workers.size());

        return Multi.createFrom().iterable(workers.stream().map(Message::of).collect(Collectors.toList()))
                .onCompletion().invoke(() -> {
                    LOG.debug("Acknowleding trigger message of schedule '{}' ...", triggerMessage.getName());
                    message.ack();
                    LOG.debug("Acknowleded trigger message of schedule '{}'", triggerMessage.getName());
                });
        */
    }
}