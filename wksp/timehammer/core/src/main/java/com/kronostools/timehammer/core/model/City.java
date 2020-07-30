package com.kronostools.timehammer.core.model;

import com.kronostools.timehammer.common.constants.SupportedTimezone;
import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;

import java.util.stream.StreamSupport;

public class City {
    private final String code;
    private final String name;
    private final SupportedTimezone timezone;

    public City(final String code, final String name, final SupportedTimezone timezone) {
        this.code = code;
        this.name = name;
        this.timezone = timezone;
    }

    private static City from(Row row) {
        return new City(row.getString("code"), row.getString("name"), SupportedTimezone.fromTimezoneName(row.getString("timezone")));
    }

    public static Multi<City> findAll(PgPool client) {
        return client.query(
                "SELECT c.code, c.name, c.timezone " +
                    "FROM city c " +
                    "ORDER BY c.name")
                .execute()
                .onItem()
                .produceMulti(set -> Multi.createFrom().items(StreamSupport.stream(set.spliterator(), false)))
                .onItem().apply(City::from);
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