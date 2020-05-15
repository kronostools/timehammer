package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class ScheduleTriggerMessageBuilder extends ScheduleMessageBuilder<ScheduleTriggerMessageBuilder> {
    public static ScheduleTriggerMessageBuilder copy(final ScheduleTriggerMessage scheduleTriggerMessage) {
        return Optional.ofNullable(scheduleTriggerMessage)
                .map(stm -> new ScheduleTriggerMessageBuilder()
                    .timestamp(stm.getTimestamp())
                    .executionId(stm.getExecutionId())
                    .name(stm.getName()))
                .orElse(null);
    }

    public ScheduleTriggerMessage build() {
        return  new ScheduleTriggerMessage(timestamp, name, executionId);
    }
}