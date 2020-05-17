package com.kronostools.timehammer.common.messages.schedules.deserializers;

import com.kronostools.timehammer.common.messages.schedules.CleanPastWorkersHolidays;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class CleanPastWorkersHolidaysJacksonDeserializer extends ObjectMapperDeserializer<CleanPastWorkersHolidays> {
    public CleanPastWorkersHolidaysJacksonDeserializer() {
        super(CleanPastWorkersHolidays.class);
    }
}