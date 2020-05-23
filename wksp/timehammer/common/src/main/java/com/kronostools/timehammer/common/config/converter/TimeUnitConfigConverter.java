package com.kronostools.timehammer.common.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

import java.util.concurrent.TimeUnit;

public class TimeUnitConfigConverter implements Converter<TimeUnit> {
    @Override
    public TimeUnit convert(String timeUnit) {
        return TimeUnit.valueOf(timeUnit);
    }
}