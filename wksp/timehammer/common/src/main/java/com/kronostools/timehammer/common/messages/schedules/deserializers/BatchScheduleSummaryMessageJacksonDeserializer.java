package com.kronostools.timehammer.common.messages.schedules.deserializers;

import com.kronostools.timehammer.common.messages.schedules.BatchScheduleSummaryMessage;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class BatchScheduleSummaryMessageJacksonDeserializer extends ObjectMapperDeserializer<BatchScheduleSummaryMessage> {
    public BatchScheduleSummaryMessageJacksonDeserializer() {
        super(BatchScheduleSummaryMessage.class);
    }
}