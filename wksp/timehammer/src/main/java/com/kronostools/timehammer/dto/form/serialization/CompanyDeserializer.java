package com.kronostools.timehammer.dto.form.serialization;

import com.kronostools.timehammer.enums.Company;

import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.stream.JsonParser;
import java.lang.reflect.Type;

public class CompanyDeserializer implements JsonbDeserializer<Company> {
    @Override
    public Company deserialize(JsonParser jsonParser, DeserializationContext deserializationContext, Type type) {
        return Company.fromCode(jsonParser.getString());
    }
}
