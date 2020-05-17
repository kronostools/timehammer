package com.kronostools.timehammer.common.messages.schedules.deserializers;

import com.kronostools.timehammer.common.messages.schedules.ScheduleSummaryMessage;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class ScheduleSummaryMessageJacksonDeserializer extends ObjectMapperDeserializer<ScheduleSummaryMessage> {
    public ScheduleSummaryMessageJacksonDeserializer() {
        super(ScheduleSummaryMessage.class);
    }
}