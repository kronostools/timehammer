package com.kronostools.timehammer.dto.form.serialization;

import com.kronostools.timehammer.enums.SupportedTimezone;

import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;

public class SupportedTimezoneSerializer implements JsonbSerializer<SupportedTimezone> {
    @Override
    public void serialize(SupportedTimezone supportedTimezone, JsonGenerator jsonGenerator, SerializationContext serializationContext) {
        jsonGenerator.write(supportedTimezone.getTimezoneName());
    }
}
