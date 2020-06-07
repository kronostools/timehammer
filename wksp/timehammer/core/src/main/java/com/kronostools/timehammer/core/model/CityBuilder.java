package com.kronostools.timehammer.core.model;

import com.kronostools.timehammer.common.constants.SupportedTimezone;
import io.vertx.mutiny.sqlclient.Row;

public class CityBuilder {
    private String code;
    private String name;
    private SupportedTimezone timezone;

    public static CityBuilder from(final Row row) {
        final String code = row.getString("code");
        final String name = row.getString("name");

        final SupportedTimezone timezone = SupportedTimezone.fromTimezoneName(row.getString("timezone"));

        return new CityBuilder()
                .code(code)
                .name(name)
                .timezone(timezone);
    }

    public CityBuilder code(final String code) {
        this.code = code;
        return this;
    }

    public CityBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public CityBuilder timezone(final SupportedTimezone timezone) {
        this.timezone = timezone;
        return this;
    }

    public City build() {
        return new City(code, name, timezone);
    }
}