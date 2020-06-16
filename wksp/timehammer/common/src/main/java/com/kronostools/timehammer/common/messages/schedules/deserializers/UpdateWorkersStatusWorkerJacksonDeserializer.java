package com.kronostools.timehammer.common.messages.schedules.deserializers;

import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersStatusWorker;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class UpdateWorkersStatusWorkerJacksonDeserializer extends ObjectMapperDeserializer<UpdateWorkersStatusWorker> {
    public UpdateWorkersStatusWorkerJacksonDeserializer() {
        super(UpdateWorkersStatusWorker.class);
    }
}