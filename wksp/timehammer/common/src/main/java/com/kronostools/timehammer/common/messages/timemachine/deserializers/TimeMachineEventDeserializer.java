package com.kronostools.timehammer.common.messages.timemachine.deserializers;

import com.kronostools.timehammer.common.messages.timemachine.TimeMachineEventMessage;
import io.quarkus.kafka.client.serialization.JsonbDeserializer;

public class TimeMachineEventDeserializer extends JsonbDeserializer<TimeMachineEventMessage> {
    public TimeMachineEventDeserializer() {
        super(TimeMachineEventMessage.class);
    }
}