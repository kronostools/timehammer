package com.kronostools.timehammer.common.messages.schedules;

import java.time.LocalDateTime;

public abstract class AbstractScheduleSummaryMessageBuilder<B> extends AbstractScheduleMessageBuilder<B> {
    protected LocalDateTime endTimestamp;

    public B endTimestamp(final LocalDateTime endTimestamp) {
        this.endTimestamp = endTimestamp;
        return (B) this;
    }
}