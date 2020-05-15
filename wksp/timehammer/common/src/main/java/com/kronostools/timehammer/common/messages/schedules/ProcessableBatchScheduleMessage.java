package com.kronostools.timehammer.common.messages.schedules;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class ProcessableBatchScheduleMessage extends BatchScheduleMessage {
    ProcessableBatchScheduleMessage(final LocalDateTime timestamp, final String name, final UUID executionId, final int batchSize) {
        super(timestamp, name, executionId, batchSize);
    }

    public abstract boolean processedSuccessfully();
}