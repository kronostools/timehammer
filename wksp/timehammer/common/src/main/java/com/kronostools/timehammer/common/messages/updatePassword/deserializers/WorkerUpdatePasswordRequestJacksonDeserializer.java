package com.kronostools.timehammer.common.messages.updatePassword.deserializers;

import com.kronostools.timehammer.common.messages.updatePassword.WorkerUpdatePasswordRequestMessage;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class WorkerUpdatePasswordRequestJacksonDeserializer extends ObjectMapperDeserializer<WorkerUpdatePasswordRequestMessage> {
    public WorkerUpdatePasswordRequestJacksonDeserializer() {
        super(WorkerUpdatePasswordRequestMessage.class);
    }
}