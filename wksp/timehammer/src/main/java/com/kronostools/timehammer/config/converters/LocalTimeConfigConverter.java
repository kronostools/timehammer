package com.kronostools.timehammer.config.converters;

import com.kronostools.timehammer.service.TimeMachineService;
import org.eclipse.microprofile.config.spi.Converter;

import java.time.LocalTime;

public class LocalTimeConfigConverter implements Converter<LocalTime> {
    @Override
    public LocalTime convert(String localTime) {
        return LocalTime.parse(localTime, TimeMachineService.FORMATTER_HHMM);
    }
}
