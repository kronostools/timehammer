package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.enums.SupportedTimezone;

public class CityVo {
    private final String code;
    private final SupportedTimezone timezone;

    public CityVo(final String code, final SupportedTimezone timezone) {
        this.code = code;
        this.timezone = timezone;
    }

    public static CityVo empty() {
        return new CityVo("", SupportedTimezone.UTC);
    }

    public String getCode() {
        return code;
    }

    public SupportedTimezone getTimezone() {
        return timezone;
    }

    public boolean isValid() {
        return !code.equals("");
    }

    @Override
    public String toString() {
        return "CityVo{" +
                "code='" + code + '\'' +
                ", timezone=" + timezone +
                '}';
    }
}