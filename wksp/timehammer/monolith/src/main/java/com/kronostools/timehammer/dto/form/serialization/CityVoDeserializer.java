package com.kronostools.timehammer.dto.form.serialization;

import com.kronostools.timehammer.service.CityService;
import com.kronostools.timehammer.vo.CityVo;

import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.stream.JsonParser;
import java.lang.reflect.Type;

public class CityVoDeserializer implements JsonbDeserializer<CityVo> {
    private final CityService cityService;

    public CityVoDeserializer(final CityService cityService) {
        this.cityService = cityService;
    }

    @Override
    public CityVo deserialize(JsonParser jsonParser, DeserializationContext deserializationContext, Type type) {
        return cityService
                .findByCode(jsonParser.getString())
                .orElse(CityVo.empty());
    }
}
