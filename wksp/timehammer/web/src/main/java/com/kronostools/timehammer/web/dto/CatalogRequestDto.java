package com.kronostools.timehammer.web.dto;

import com.kronostools.timehammer.common.constants.CatalogType;

import java.util.List;
import java.util.Map;

public class CatalogRequestDto extends Dto {
    private String requesterId;
    private List<CatalogType> requestedCatalogs;
    private Map<CatalogType, CatalogResponseDto> catalogResponses;

    public CatalogRequestDto() {
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

    public List<CatalogType> getRequestedCatalogs() {
        return requestedCatalogs;
    }

    public void setRequestedCatalogs(List<CatalogType> requestedCatalogs) {
        this.requestedCatalogs = requestedCatalogs;
    }

    public Map<CatalogType, CatalogResponseDto> getCatalogResponses() {
        return catalogResponses;
    }

    public void setCatalogResponses(Map<CatalogType, CatalogResponseDto> catalogResponses) {
        this.catalogResponses = catalogResponses;
    }
}