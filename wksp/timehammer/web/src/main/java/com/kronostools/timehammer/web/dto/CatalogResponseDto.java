package com.kronostools.timehammer.web.dto;

import com.kronostools.timehammer.common.constants.CatalogType;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

import java.util.List;

public class CatalogResponseDto extends Dto {
    private final CatalogType catalogType;
    private final SimpleResult result;
    private final String errorMessage;
    private List<CatalogElementDto> elements;

    public CatalogResponseDto(final CatalogType catalogType, final SimpleResult result, final String errorMessage) {
        this.catalogType = catalogType;
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public CatalogType getCatalogType() {
        return catalogType;
    }

    public SimpleResult getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<CatalogElementDto> getElements() {
        return elements;
    }

    public void setElements(List<CatalogElementDto> elements) {
        this.elements = elements;
    }
}