package com.kronostools.timehammer.common.messages.schedules;

import com.kronostools.timehammer.common.messages.PlatformMessage;

import java.time.LocalDateTime;
import java.util.UUID;

public class ScheduleTriggerMessage extends PlatformMessage {
    private String name;
    private UUID executionId;

    public static class Builder {
        private LocalDateTime timestamp;
        private final UUID executionId;
        private String name;

        Builder() {
            this.executionId = UUID.randomUUID();
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder timestamp(final LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public ScheduleTriggerMessage build() {
            ScheduleTriggerMessage result =  new ScheduleTriggerMessage();
            result.setTimestamp(timestamp);
            result.setExecutionId(executionId);
            result.setName(name);

            return result;
        }
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