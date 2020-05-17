package com.kronostools.timehammer.common.messages.schedules;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class ProcessableScheduleMessage extends ScheduleMessage {
    ProcessableScheduleMessage(final LocalDateTime timestamp, final String name, final UUID executionId) {
        super(timestamp, name, executionId);
    }

    public abstract boolean processedSuccessfully();
}