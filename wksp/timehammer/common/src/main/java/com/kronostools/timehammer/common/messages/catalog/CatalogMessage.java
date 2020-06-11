package com.kronostools.timehammer.common.messages.catalog;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.constants.CatalogType;
import com.kronostools.timehammer.common.messages.PlatformMessage;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Map;

@JsonDeserialize(builder = CatalogMessageBuilder.class)
public class CatalogMessage extends PlatformMessage {
    private final String requesterId;
    private final LinkedList<CatalogType> requestedCatalogs;
    private final Map<CatalogType, CatalogResponsePhase> catalogResponses;

    CatalogMessage(final LocalDateTime generated, final String requesterId, final LinkedList<CatalogType> requestedCatalogs, final Map<CatalogType, CatalogResponsePhase> catalogResponses) {
        super(generated);
        this.requesterId = requesterId;
        this.requestedCatalogs = requestedCatalogs;
        this.catalogResponses = catalogResponses;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public LinkedList<CatalogType> getRequestedCatalogs() {
        return requestedCatalogs;
    }

    public Map<CatalogType, CatalogResponsePhase> getCatalogResponses() {
        return catalogResponses;
    }
}