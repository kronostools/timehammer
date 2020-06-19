package com.kronostools.timehammer.scheduler.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorker;
import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorkerBuilder;
import com.kronostools.timehammer.scheduler.service.ScheduleSummarizerService;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class UpdateWorkersStatusSummarizer {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateWorkersStatusSummarizer.class);

    private final ScheduleSummarizerService scheduleSummarizerService;

    public UpdateWorkersStatusSummarizer(final ScheduleSummarizerService scheduleSummarizerService) {
        this.scheduleSummarizerService = scheduleSummarizerService;
    }

    @Incoming(Channels.STATUS_WORKER_NOTIFY)
    public CompletionStage<Void> process(final Message<CheckWorkersStatusWorker> message) {
        final CheckWorkersStatusWorker worker = CheckWorkersStatusWorkerBuilder.copy(message.getPayload()).build();

        LOG.info("Processing new batch scheduled message of schedule '{}' (execution id: '{}')", worker.getName(), worker.getExecutionId());

        return scheduleSummarizerService.processBatchScheduleMessage(message);
    }
}