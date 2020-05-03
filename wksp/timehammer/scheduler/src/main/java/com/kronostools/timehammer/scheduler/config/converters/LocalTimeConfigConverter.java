package com.kronostools.timehammer.scheduler.config.converters;

import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import org.eclipse.microprofile.config.spi.Converter;

import java.time.LocalTime;

public class LocalTimeConfigConverter implements Converter<LocalTime> {
    @Override
    public LocalTime convert(String localTime) {
        return CommonDateTimeUtils.parseTimeFromConfig(localTime);
    }
}