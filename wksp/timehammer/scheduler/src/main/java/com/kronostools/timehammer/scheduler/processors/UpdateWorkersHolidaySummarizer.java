package com.kronostools.timehammer.scheduler.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorker;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorkerBuilder;
import com.kronostools.timehammer.scheduler.service.ScheduleSummarizerService;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class UpdateWorkersHolidaySummarizer {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateWorkersHolidaySummarizer.class);

    private final ScheduleSummarizerService scheduleSummarizerService;

    public UpdateWorkersHolidaySummarizer(final ScheduleSummarizerService scheduleSummarizerService) {
        this.scheduleSummarizerService = scheduleSummarizerService;
    }

    @Incoming(Channels.HOLIDAYS_WORKER_SUMMARY)
    public CompletionStage<Void> process(final Message<UpdateWorkersHolidayWorker> message) {
        final UpdateWorkersHolidayWorker worker = UpdateWorkersHolidayWorkerBuilder.copy(message.getPayload()).build();

        LOG.info("Processing new batch scheduled message of schedule '{}' (execution id: '{}')", worker.getName(), worker.getExecutionId());

        return scheduleSummarizerService.processBatchScheduleMessage(message);
    }
}