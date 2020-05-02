package com.kronostools.timehammer.common.messages.schedules.deserializers;

import io.quarkus.kafka.client.serialization.JsonbDeserializer;

public class UpdateWorkersHolidays extends JsonbDeserializer<UpdateWorkersHolidays> {
    public UpdateWorkersHolidays() {
        super(UpdateWorkersHolidays.class);
    }
}