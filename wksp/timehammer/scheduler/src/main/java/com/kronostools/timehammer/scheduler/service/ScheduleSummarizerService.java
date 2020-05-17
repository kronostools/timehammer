package com.kronostools.timehammer.scheduler.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.*;
import com.kronostools.timehammer.common.services.TimeMachineService;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class ScheduleSummarizerService {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleSummarizerService.class);

    private final Cache<UUID, BatchScheduleSummaryMessage> batchedSchedulesCache;
    private final Emitter<BatchScheduleSummaryMessage> batchScheduleSummaryEmitter;
    private final Emitter<ScheduleSummaryMessage> scheduleSummaryEmitter;
    private final TimeMachineService timeMachineService;


    public ScheduleSummarizerService(@Channel(Channels.BATCH_SCHEDULE_SUMMARY) final Emitter<BatchScheduleSummaryMessage> batchScheduleSummaryEmitter,
                                     @Channel(Channels.SCHEDULE_SUMMARY) final Emitter<ScheduleSummaryMessage> scheduleSummaryEmitter,
                                     final TimeMachineService timeMachineService) {
        this.batchedSchedulesCache = Caffeine.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .build();

        this.batchScheduleSummaryEmitter = batchScheduleSummaryEmitter;
        this.scheduleSummaryEmitter = scheduleSummaryEmitter;
        this.timeMachineService = timeMachineService;
    }

    public CompletionStage<Void> processBatchScheduleMessage(final Message<? extends ProcessableBatchScheduleMessage> message) {
        final ProcessableBatchScheduleMessage batchScheduleMessage = message.getPayload();

        final BatchScheduleSummaryMessageBuilder summaryMessageBuilder = BatchScheduleSummaryMessageBuilder.copy(batchedSchedulesCache.get(batchScheduleMessage.getExecutionId(), (k) -> BatchScheduleSummaryMessageBuilder.copyAndBuild(batchScheduleMessage)));

        final BatchScheduleSummaryMessage summaryMessage = summaryMessageBuilder
                .process(batchScheduleMessage.processedSuccessfully(), timeMachineService.getNow())
                .build();

        if (summaryMessage.isFinished()) {
            LOG.info("Finished processing all {} elements in batch of schedule '{}' (execution id: '{}') in '{}'", summaryMessage.getProcessed(), summaryMessage.getName(), summaryMessage.getExecutionId(), Duration.between(summaryMessage.getGenerated(), summaryMessage.getEndTimestamp()).toString());

            return batchScheduleSummaryEmitter.send(summaryMessage).handle((Void, e) -> {
                if (e == null) {
                    batchedSchedulesCache.invalidate(summaryMessage.getExecutionId());
                    message.ack();
                } else {
                    LOG.warn("There was an error publishing summary of batched schedule '{}' (execution id: '{}')", summaryMessage.getName(), summaryMessage.getExecutionId());
                }

                return null;
            });
        } else {
            LOG.info("Processed element {} out of {} of schedule '{}' (execution id: '{}')", summaryMessage.getProcessed(), summaryMessage.getBatchSize(), summaryMessage.getName(), summaryMessage.getExecutionId());

            batchedSchedulesCache.put(summaryMessage.getExecutionId(), summaryMessage);
            return message.ack();
        }
    }

    public CompletionStage<Void> processScheduleMessage(final Message<? extends ProcessableScheduleMessage> message) {
        final ProcessableScheduleMessage scheduleMessage = message.getPayload();

        final ScheduleSummaryMessage summaryMessage = ScheduleSummaryMessageBuilder.copy(scheduleMessage)
                .endTimestamp(timeMachineService.getNow())
                .processedSuccessfully(scheduleMessage.processedSuccessfully())
                .build();

        LOG.info("Finished processing schedule '{}' (execution id: '{}') in '{}'", summaryMessage.getName(), summaryMessage.getExecutionId(), Duration.between(summaryMessage.getGenerated(), summaryMessage.getEndTimestamp()).toString());

        return scheduleSummaryEmitter.send(summaryMessage).handle((Void, e) -> {
            if (e == null) {
                message.ack();
            } else {
                LOG.warn("There was an error publishing summary of schedule '{}' (execution id: '{}')", summaryMessage.getName(), summaryMessage.getExecutionId());
            }

            return null;
        });
    }
}