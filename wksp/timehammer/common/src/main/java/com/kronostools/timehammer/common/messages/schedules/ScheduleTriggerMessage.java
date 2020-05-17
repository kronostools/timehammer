package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonDeserialize(builder = ScheduleTriggerMessageBuilder.class)
public class ScheduleTriggerMessage extends ScheduleMessage {
    ScheduleTriggerMessage(final LocalDateTime generated, final String name, final UUID executionId) {
        super(generated, name, executionId);
    }
}