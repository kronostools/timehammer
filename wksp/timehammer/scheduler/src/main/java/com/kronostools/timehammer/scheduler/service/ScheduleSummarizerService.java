package com.kronostools.timehammer.scheduler.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.BatchScheduleSummaryMessage;
import com.kronostools.timehammer.common.messages.schedules.BatchScheduleSummaryMessageBuilder;
import com.kronostools.timehammer.common.messages.schedules.ProcessableBatchScheduleMessage;
import com.kronostools.timehammer.common.services.TimeMachineService;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class ScheduleSummarizerService {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleSummarizerService.class);

    private final Cache<UUID, BatchScheduleSummaryMessage> cache;
    private final Emitter<BatchScheduleSummaryMessage> scheduleSummaryEmitter;
    private final TimeMachineService timeMachineService;


    public ScheduleSummarizerService(@Channel(Channels.SCHEDULE_SUMMARY) final Emitter<BatchScheduleSummaryMessage> scheduleSummaryEmitter,
                                     final TimeMachineService timeMachineService) {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .build();

        this.scheduleSummaryEmitter = scheduleSummaryEmitter;
        this.timeMachineService = timeMachineService;
    }

    public CompletionStage<Void> processBatchScheduleMessage(final Message<? extends ProcessableBatchScheduleMessage> message) {
        final ProcessableBatchScheduleMessage batchScheduleMessage = message.getPayload();

        final BatchScheduleSummaryMessageBuilder summaryMessageBuilder = BatchScheduleSummaryMessageBuilder.copy(cache.get(batchScheduleMessage.getExecutionId(), (k) -> BatchScheduleSummaryMessageBuilder.copyAndBuild(batchScheduleMessage)));

        final BatchScheduleSummaryMessage summaryMessage = summaryMessageBuilder
                .process(batchScheduleMessage.processedSuccessfully(), timeMachineService.getNow())
                .build();

        if (summaryMessage.isFinished()) {
            LOG.info("Finished processing all {} elements in batch of schedule '{}' (execution id: '{}') in '{}'", summaryMessage.getProcessed(), summaryMessage.getName(), summaryMessage.getExecutionId(), Duration.between(summaryMessage.getTimestamp(), summaryMessage.getEndTimestamp()).toString());

            return scheduleSummaryEmitter.send(summaryMessage).handle((Void, e) -> {
                if (e == null) {
                    cache.invalidate(summaryMessage.getExecutionId());
                    message.ack();
                } else {
                    LOG.warn("There was an error publishing summary of schedule '{}' (execution id: '{}')", summaryMessage.getName(), summaryMessage.getExecutionId());
                }

                return null;
            });
        } else {
            LOG.info("Processed element {} out of {} of schedule '{}' (execution id: '{}')", summaryMessage.getProcessed(), summaryMessage.getBatchSize(), summaryMessage.getName(), summaryMessage.getExecutionId());

            cache.put(summaryMessage.getExecutionId(), summaryMessage);
            return CompletableFuture.completedStage(null);
        }
    }
}