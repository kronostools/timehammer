package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.PlatformMessageBuilder;

import java.util.Optional;
import java.util.UUID;

@JsonPOJOBuilder(withPrefix = "")
public class ScheduleTriggerMessageBuilder extends PlatformMessageBuilder<ScheduleTriggerMessageBuilder> {
    private UUID executionId;
    private String name;

    public static ScheduleTriggerMessageBuilder copy(final ScheduleTriggerMessage scheduleTriggerMessage) {
        return Optional.ofNullable(scheduleTriggerMessage)
                .map(stm -> new ScheduleTriggerMessageBuilder()
                    .timestamp(stm.getTimestamp())
                    .executionId(stm.getExecutionId())
                    .name(stm.getName()))
                .orElse(null);
    }

    public ScheduleTriggerMessageBuilder executionId(final UUID executionId) {
        this.executionId = executionId;
        return this;
    }

    public ScheduleTriggerMessageBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public ScheduleTriggerMessage build() {
        ScheduleTriggerMessage result =  new ScheduleTriggerMessage(timestamp);
        result.setExecutionId(executionId);
        result.setName(name);

        return result;
    }
}