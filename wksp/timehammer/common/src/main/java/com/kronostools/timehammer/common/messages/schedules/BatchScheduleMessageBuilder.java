package com.kronostools.timehammer.common.messages.schedules;

public abstract class BatchScheduleMessageBuilder<B> extends ScheduleMessageBuilder<B> {
    protected Integer batchSize;

    public B batchSize(final Integer batchSize) {
        this.batchSize = batchSize;
        return (B) this;
    }
}