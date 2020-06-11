package com.kronostools.timehammer.common.messages.catalog;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.CatalogType;
import com.kronostools.timehammer.common.messages.PlatformMessageBuilder;

import java.util.*;

@JsonPOJOBuilder(withPrefix = "")
public class CatalogMessageBuilder extends PlatformMessageBuilder<CatalogMessageBuilder> {
    private String requesterId;
    private LinkedList<CatalogType> requestedCatalogs;
    private Map<CatalogType, CatalogResponsePhase> catalogResponses;

    public static CatalogMessageBuilder copy(final CatalogMessage catalogMessage) {
        return Optional.ofNullable(catalogMessage)
                .map(cm -> new CatalogMessageBuilder()
                        .generated(cm.getGenerated())
                        .requesterId(cm.getRequesterId())
                        .requestedCatalogs(cm.getRequestedCatalogs())
                        .catalogResponses(cm.getCatalogResponses()))
                .orElse(null);
    }

    public CatalogMessageBuilder requesterId(final String requesterId) {
        this.requesterId = requesterId;
        return this;
    }

    public CatalogMessageBuilder requestedCatalogs(final List<CatalogType> requestedCatalogs) {
        this.requestedCatalogs = new LinkedList<CatalogType>() {{
            addAll(requestedCatalogs);
        }};
        
        return this;
    }

    public CatalogMessageBuilder requestedCatalogs(final LinkedList<CatalogType> requestedCatalogs) {
        this.requestedCatalogs = requestedCatalogs;
        return this;
    }

    public CatalogMessageBuilder catalogResponses(final Map<CatalogType, CatalogResponsePhase> catalogResponses) {
        this.catalogResponses = catalogResponses;
        return this;
    }

    public CatalogMessageBuilder putCatalogResponse(final CatalogType catalogType, final CatalogResponsePhase catalogResponse) {
        if (catalogResponses == null) {
            catalogResponses = new HashMap<>();
        }

        catalogResponses.put(catalogType, catalogResponse);
        return this;
    }

    public CatalogMessage build() {
        return new CatalogMessage(generated, requesterId, requestedCatalogs, catalogResponses);
    }
}