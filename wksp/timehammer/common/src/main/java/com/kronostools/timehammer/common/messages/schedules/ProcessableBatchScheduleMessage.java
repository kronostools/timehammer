package com.kronostools.timehammer.common.messages.schedules;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class ProcessableBatchScheduleMessage extends ProcessableScheduleMessage {
    protected int batchSize;

    ProcessableBatchScheduleMessage(final LocalDateTime timestamp, final String name, final UUID executionId, final int batchSize) {
        super(timestamp, name, executionId);
        this.batchSize = batchSize;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}