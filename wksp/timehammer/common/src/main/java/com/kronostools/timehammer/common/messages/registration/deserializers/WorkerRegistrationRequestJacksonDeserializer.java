package com.kronostools.timehammer.common.messages.registration.deserializers;

import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequest;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class WorkerRegistrationRequestJacksonDeserializer extends ObjectMapperDeserializer<WorkerRegistrationRequest> {
    public WorkerRegistrationRequestJacksonDeserializer() {
        super(WorkerRegistrationRequest.class);
    }
}