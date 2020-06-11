package com.kronostools.timehammer.web.dto;

import com.kronostools.timehammer.common.constants.CatalogType;

import java.util.HashMap;
import java.util.Map;

public class CatalogRequestDtoBuilder {
    private String requesterId;
    private Map<CatalogType, CatalogResponseDto> catalogResponses;

    public CatalogRequestDtoBuilder() {
        catalogResponses = new HashMap<>();
    }

    public CatalogRequestDtoBuilder requesterId(final String requesterId) {
        this.requesterId = requesterId;
        return this;
    }

    public void putCatalogResponse(final CatalogType catalogType, final CatalogResponseDto catalogResponseDto) {
        catalogResponses.put(catalogType, catalogResponseDto);
    }

    public CatalogRequestDto build() {
        final CatalogRequestDto cr = new CatalogRequestDto();
        cr.setRequesterId(requesterId);
        cr.setCatalogResponses(catalogResponses);

        return cr;
    }
}