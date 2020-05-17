package com.kronostools.timehammer.scheduler.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.schedules.CleanPastWorkersHolidays;
import com.kronostools.timehammer.common.messages.schedules.CleanPastWorkersHolidaysBuilder;
import com.kronostools.timehammer.scheduler.service.ScheduleSummarizerService;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class CleanPastWorkersHolidaysSummarizer {
    private static final Logger LOG = LoggerFactory.getLogger(CleanPastWorkersHolidaysSummarizer.class);

    private final ScheduleSummarizerService scheduleSummarizerService;

    public CleanPastWorkersHolidaysSummarizer(final ScheduleSummarizerService scheduleSummarizerService) {
        this.scheduleSummarizerService = scheduleSummarizerService;
    }

    @Incoming(Channels.HOLIDAYS_CLEAN_SUMMARY)
    public CompletionStage<Void> process(final Message<CleanPastWorkersHolidays> message) {
        final CleanPastWorkersHolidays cleanPastWorkersHolidays = CleanPastWorkersHolidaysBuilder.copy(message.getPayload()).build();

        LOG.info("Processing new message of schedule '{}' (execution id: '{}')", cleanPastWorkersHolidays.getName(), cleanPastWorkersHolidays.getExecutionId());

        return scheduleSummarizerService.processScheduleMessage(message);
    }
}