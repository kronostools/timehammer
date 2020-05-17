package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class ScheduleTriggerMessageBuilder extends AbstractScheduleMessageBuilder<ScheduleTriggerMessageBuilder> {
    public static ScheduleTriggerMessageBuilder copy(final ScheduleTriggerMessage scheduleTriggerMessage) {
        return Optional.ofNullable(scheduleTriggerMessage)
                .map(stm -> new ScheduleTriggerMessageBuilder()
                    .generated(stm.getGenerated())
                    .name(stm.getName())
                    .executionId(stm.getExecutionId()))
                .orElse(null);
    }

    public ScheduleTriggerMessage build() {
        return  new ScheduleTriggerMessage(generated, name, executionId);
    }
}