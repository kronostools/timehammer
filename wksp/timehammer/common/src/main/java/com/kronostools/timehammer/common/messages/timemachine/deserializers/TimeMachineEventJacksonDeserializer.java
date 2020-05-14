package com.kronostools.timehammer.common.messages.timemachine.deserializers;

import com.kronostools.timehammer.common.messages.timemachine.TimeMachineEventMessage;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class TimeMachineEventJacksonDeserializer extends ObjectMapperDeserializer<TimeMachineEventMessage> {
    public TimeMachineEventJacksonDeserializer() {
        super(TimeMachineEventMessage.class);
    }
}