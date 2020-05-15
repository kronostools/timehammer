package com.kronostools.timehammer.common.messages.schedules;

import com.kronostools.timehammer.common.messages.PlatformMessage;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class ScheduleMessage extends PlatformMessage {
    protected final String name;
    protected final UUID executionId;

    ScheduleMessage(final LocalDateTime timestamp, final String name, final UUID executionId) {
        super(timestamp);
        this.name = name;
        this.executionId = executionId;
    }

    public String getName() {
        return name;
    }

    public UUID getExecutionId() {
        return executionId;
    }
}