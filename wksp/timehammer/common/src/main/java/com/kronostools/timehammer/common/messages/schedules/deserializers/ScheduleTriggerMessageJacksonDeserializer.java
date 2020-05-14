package com.kronostools.timehammer.common.messages.schedules.deserializers;

import com.kronostools.timehammer.common.messages.schedules.ScheduleTriggerMessage;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class ScheduleTriggerMessageJacksonDeserializer extends ObjectMapperDeserializer<ScheduleTriggerMessage> {
    public ScheduleTriggerMessageJacksonDeserializer() {
        super(ScheduleTriggerMessage.class);
    }
}