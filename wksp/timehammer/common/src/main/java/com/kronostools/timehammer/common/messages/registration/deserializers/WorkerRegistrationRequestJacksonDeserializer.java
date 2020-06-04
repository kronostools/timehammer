package com.kronostools.timehammer.common.messages.registration.deserializers;

import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessage;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class WorkerRegistrationRequestJacksonDeserializer extends ObjectMapperDeserializer<WorkerRegistrationRequestMessage> {
    public WorkerRegistrationRequestJacksonDeserializer() {
        super(WorkerRegistrationRequestMessage.class);
    }
}