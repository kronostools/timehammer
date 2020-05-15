package com.kronostools.timehammer.common.messages.schedules;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BatchScheduleMessage extends ScheduleMessage {
    protected final int batchSize;

    BatchScheduleMessage(final LocalDateTime timestamp, final String name, final UUID executionId, final int batchSize) {
        super(timestamp, name, executionId);
        this.batchSize = batchSize;
    }

    public int getBatchSize() {
        return batchSize;
    }
}