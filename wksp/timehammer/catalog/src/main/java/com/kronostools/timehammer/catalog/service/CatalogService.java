package com.kronostools.timehammer.catalog.service;

import com.kronostools.timehammer.common.constants.CatalogType;
import com.kronostools.timehammer.common.messages.catalog.CatalogResponsePhase;
import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CatalogService {
    private static final Logger LOG = LoggerFactory.getLogger(CatalogService.class);

    public Uni<CatalogResponsePhase> getCatalog(final CatalogType catalogType) {
        return null;
    }
}