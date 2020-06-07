package com.kronostools.timehammer.core.dao;

import com.kronostools.timehammer.common.utils.CommonUtils;
import com.kronostools.timehammer.core.model.City;
import com.kronostools.timehammer.core.model.CityBuilder;
import com.kronostools.timehammer.core.model.CityMultipleResult;
import com.kronostools.timehammer.core.model.CityMultipleResultBuilder;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CityDao {
    private static final Logger LOG = LoggerFactory.getLogger(CityDao.class);

    private final PgPool client;

    public CityDao(final PgPool client) {
        this.client = client;
    }

    public Uni<CityMultipleResult> findAll() {
        return client.preparedQuery(
                "SELECT c.code, c.name, c.timezone " +
                    "FROM city c " +
                    "ORDER BY c.name"
                )
                .mapping(row -> CityBuilder.from(row).build())
                .execute()
                .map(pgRowSet -> {
                    final List<City> list = new ArrayList<>(pgRowSet.rowCount());

                    pgRowSet.forEach(list::add);

                    return CityMultipleResultBuilder.buildFromResult(list);
                })
                .onFailure()
                    .recoverWithItem((e) -> {
                        final String message = CommonUtils.stringFormat("There was an unexpected error getting list of all cities");

                        LOG.error("{}. Reason: {}", message, e.getMessage());

                        return CityMultipleResultBuilder.buildFromError(message);
                    });
    }
}