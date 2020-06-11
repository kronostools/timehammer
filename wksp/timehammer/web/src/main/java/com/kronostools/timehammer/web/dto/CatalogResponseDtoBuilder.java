package com.kronostools.timehammer.web.dto;

import com.kronostools.timehammer.common.constants.CatalogType;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;
import com.kronostools.timehammer.common.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class CatalogResponseDtoBuilder {
    private final CatalogType catalogType;
    private final SimpleResult result;
    private String errorMessage;
    private List<CatalogElementDto> elements;

    public CatalogResponseDtoBuilder(final CatalogType catalogType, final SimpleResult result) {
        this.catalogType = catalogType;
        this.result = result;
        this.elements = new ArrayList<>();

        if (result == SimpleResult.KO) {
            errorMessage = CommonUtils.stringFormat("Se ha producido un error al recuperar los datos del catálogo '{}'. Por favor refresca la página, y si el error persiste, contacta con el administrador del sitio.", catalogType.getLabel());
        }
    }

    public CatalogResponseDtoBuilder elements(final List<CatalogElementDto> elements) {
        this.elements = elements;
        return this;
    }

    public CatalogResponseDto build() {
        final CatalogResponseDto cr = new CatalogResponseDto(catalogType, result, errorMessage);
        cr.setElements(elements);

        return cr;
    }
}