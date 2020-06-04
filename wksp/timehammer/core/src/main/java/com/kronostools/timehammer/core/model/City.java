package com.kronostools.timehammer.core.model;

import com.kronostools.timehammer.common.constants.SupportedTimezone;

public class City {
    private final String code;
    private final String name;
    private final SupportedTimezone timezone;

    public City(final String code, final String name, final SupportedTimezone timezone) {
        this.code = code;
        this.name = name;
        this.timezone = timezone;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public SupportedTimezone getTimezone() {
        return timezone;
    }
}