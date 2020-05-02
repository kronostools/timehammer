package com.kronostools.timehammer.common.messages.schedules.deserializers;

import com.kronostools.timehammer.common.messages.schedules.ScheduleTriggerMessage;
import io.quarkus.kafka.client.serialization.JsonbDeserializer;

public class ScheduleTriggerMessageDeserializer extends JsonbDeserializer<ScheduleTriggerMessage> {
    public ScheduleTriggerMessageDeserializer() {
        super(ScheduleTriggerMessage.class);
    }
}