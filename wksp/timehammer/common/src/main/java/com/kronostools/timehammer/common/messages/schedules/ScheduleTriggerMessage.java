package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.PlatformMessage;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonDeserialize(builder = ScheduleTriggerMessageBuilder.class)
public class ScheduleTriggerMessage extends PlatformMessage {
    private String name;
    private UUID executionId;

    ScheduleTriggerMessage(final LocalDateTime timestamp) {
        super(timestamp);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getExecutionId() {
        return executionId;
    }

    public void setExecutionId(UUID executionId) {
        this.executionId = executionId;
    }
}