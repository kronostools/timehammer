package com.kronostools.timehammer.dto.form;

import com.kronostools.timehammer.dto.form.serialization.SupportedTimezoneDeserializer;
import com.kronostools.timehammer.dto.form.serialization.SupportedTimezoneSerializer;
import io.quarkus.jsonb.JsonbConfigCustomizer;

import javax.inject.Singleton;
import javax.json.bind.JsonbConfig;

@Singleton
public class CustomJsonbConfig implements JsonbConfigCustomizer {
    @Override
    public void customize(JsonbConfig jsonbConfig) {
        jsonbConfig
                .withSerializers(new SupportedTimezoneSerializer())
                .withDeserializers(new SupportedTimezoneDeserializer());
    }
}