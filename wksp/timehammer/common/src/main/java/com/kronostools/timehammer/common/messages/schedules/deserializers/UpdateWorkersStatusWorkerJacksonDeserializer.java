package com.kronostools.timehammer.common.messages.schedules.deserializers;

import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorker;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class UpdateWorkersStatusWorkerJacksonDeserializer extends ObjectMapperDeserializer<CheckWorkersStatusWorker> {
    public UpdateWorkersStatusWorkerJacksonDeserializer() {
        super(CheckWorkersStatusWorker.class);
    }
}