package com.kronostools.timehammer.common.messages.schedules;

public abstract class ProcessableBatchScheduleMessageBuilder<B> extends ProcessableScheduleMessageBuilder<B> {
    protected int batchSize;

    public B batchSize(final int batchSize) {
        this.batchSize = batchSize;
        return (B) this;
    }
}