package com.kronostools.timehammer.common.messages.schedules.deserializers;

import com.kronostools.timehammer.common.messages.schedules.UpdateWorkersHolidaysWorker;
import io.quarkus.kafka.client.serialization.JsonbDeserializer;

public class UpdateWorkersHolidaysWorkerDeserializer extends JsonbDeserializer<UpdateWorkersHolidaysWorker> {
    public UpdateWorkersHolidaysWorkerDeserializer() {
        super(UpdateWorkersHolidaysWorker.class);
    }
}