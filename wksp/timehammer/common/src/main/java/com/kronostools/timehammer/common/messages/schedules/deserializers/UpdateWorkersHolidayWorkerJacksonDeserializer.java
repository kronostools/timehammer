package com.kronostools.timehammer.common.messages.schedules.deserializers;

import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidayWorker;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class UpdateWorkersHolidayWorkerJacksonDeserializer extends ObjectMapperDeserializer<UpdateWorkersHolidayWorker> {
    public UpdateWorkersHolidayWorkerJacksonDeserializer() {
        super(UpdateWorkersHolidayWorker.class);
    }
}