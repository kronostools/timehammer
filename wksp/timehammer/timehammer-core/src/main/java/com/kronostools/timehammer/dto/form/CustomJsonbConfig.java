package com.kronostools.timehammer.dto.form;

import com.kronostools.timehammer.dto.form.serialization.CityVoDeserializer;
import com.kronostools.timehammer.dto.form.serialization.CompanyDeserializer;
import com.kronostools.timehammer.dto.form.serialization.SupportedTimezoneDeserializer;
import com.kronostools.timehammer.service.CityService;
import io.quarkus.jsonb.JsonbConfigCustomizer;

import javax.inject.Singleton;
import javax.json.bind.JsonbConfig;

@Singleton
public class CustomJsonbConfig implements JsonbConfigCustomizer {
    private final CityService cityService;

    public CustomJsonbConfig(final CityService cityService) {
        this.cityService = cityService;
    }
    @Override
    public void customize(JsonbConfig jsonbConfig) {
        jsonbConfig
                .withDeserializers(
                        new SupportedTimezoneDeserializer(),
                        new CompanyDeserializer(),
                        new CityVoDeserializer(cityService));
    }
}