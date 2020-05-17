package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonDeserialize(builder = ScheduleSummaryMessageBuilder.class)
public class ScheduleSummaryMessage extends AbstractScheduleSummaryMessage {
    private boolean processedSuccessfully;

    ScheduleSummaryMessage(final LocalDateTime generated, final String name, final UUID executionId, final LocalDateTime endTimestamp, final boolean processedSuccessfully) {
        super(generated, name, executionId, endTimestamp);
        this.processedSuccessfully = processedSuccessfully;
    }

    public boolean isProcessedSuccessfully() {
        return processedSuccessfully;
    }

    public void setProcessedSuccessfully(boolean processedSuccessfully) {
        this.processedSuccessfully = processedSuccessfully;
    }
}