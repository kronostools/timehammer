package com.kronostools.timehammer.core.dao;

import com.kronostools.timehammer.core.model.City;
import com.kronostools.timehammer.core.model.CityMultipleResult;
import com.kronostools.timehammer.core.model.CityMultipleResultBuilder;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CityService {
    private static final Logger LOG = LoggerFactory.getLogger(CityService.class);

    private final PgPool client;

    public CityService(final PgPool client) {
        this.client = client;
    }

    public Uni<CityMultipleResult> findAll() {
        return City.findAll(client)
                .collectItems().asList()
                .map(CityMultipleResultBuilder::buildFromResult)
                .onFailure().recoverWithItem(e -> {
                    final String errorMessage = "There was an unexpected error getting list of all cities";
                    LOG.error(errorMessage, e);

                    return CityMultipleResultBuilder.buildFromError(errorMessage);
                });
    }
}