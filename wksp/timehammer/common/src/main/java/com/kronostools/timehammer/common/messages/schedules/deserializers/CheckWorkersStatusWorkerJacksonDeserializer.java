package com.kronostools.timehammer.common.messages.schedules.deserializers;

import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorker;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class CheckWorkersStatusWorkerJacksonDeserializer extends ObjectMapperDeserializer<CheckWorkersStatusWorker> {
    public CheckWorkersStatusWorkerJacksonDeserializer() {
        super(CheckWorkersStatusWorker.class);
    }
}