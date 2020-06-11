package com.kronostools.timehammer.common.messages.catalog.deserializers;

import com.kronostools.timehammer.common.messages.catalog.CatalogMessage;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class CatalogMessageJacksonDeserializer extends ObjectMapperDeserializer<CatalogMessage> {
    public CatalogMessageJacksonDeserializer() {
        super(CatalogMessage.class);
    }
}