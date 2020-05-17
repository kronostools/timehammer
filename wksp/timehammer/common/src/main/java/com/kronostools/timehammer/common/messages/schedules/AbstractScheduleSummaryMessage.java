package com.kronostools.timehammer.common.messages.schedules;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class AbstractScheduleSummaryMessage extends ScheduleMessage {
    protected LocalDateTime endTimestamp;

    AbstractScheduleSummaryMessage(final LocalDateTime timestamp, final String name, final UUID executionId, final LocalDateTime endTimestamp) {
        super(timestamp, name, executionId);
        this.endTimestamp = endTimestamp;
    }

    public LocalDateTime getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(LocalDateTime endTimestamp) {
        this.endTimestamp = endTimestamp;
    }
}