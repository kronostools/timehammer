package com.kronostools.timehammer.catalog.service;

import com.kronostools.timehammer.catalog.model.City;
import com.kronostools.timehammer.common.constants.CatalogType;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.catalog.CatalogElementBuilder;
import com.kronostools.timehammer.common.messages.catalog.CatalogResponsePhase;
import com.kronostools.timehammer.common.messages.catalog.CatalogResponsePhaseBuilder;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collections;

@ApplicationScoped
public class CatalogService {
    private static final Logger LOG = LoggerFactory.getLogger(CatalogService.class);

    private final PgPool client;

    public CatalogService(final PgPool client) {
        this.client = client;
    }

    public Uni<CatalogResponsePhase> getCatalog(final CatalogType catalogType) {
        switch(catalogType) {
            case COMPANY:
                return Multi.createFrom().items(Company.values())
                        .map(c -> new CatalogElementBuilder()
                                .code(c.getCode())
                                .label(c.getText())
                                .build())
                        .collectItems().asList()
                        .map(el -> new CatalogResponsePhaseBuilder().elements(el).build());
            case CITY:
                return City.findAll(client)
                        .map(c -> new CatalogElementBuilder()
                                .code(c.getCode())
                                .label(c.getName())
                                .build())
                        .collectItems().asList()
                        .map(el -> new CatalogResponsePhaseBuilder().elements(el).build())
                        .onFailure()
                            .recoverWithItem(e -> {
                                final String errorMessage = "There was an unexpected error getting list of all cities";
                                LOG.error(errorMessage, e);

                                return new CatalogResponsePhaseBuilder().errorMessage(errorMessage).build();
                            });
            default:
                return Uni.createFrom()
                        .item(new CatalogResponsePhaseBuilder()
                                .elements(Collections.emptyList())
                                .build());
        }
    }
}