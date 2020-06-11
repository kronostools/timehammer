package com.kronostools.timehammer.common.messages.catalog;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.Phase;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

import java.util.List;

@JsonDeserialize(builder = CatalogResponsePhaseBuilder.class)
public class CatalogResponsePhase extends Phase<SimpleResult> {
    private final List<CatalogElement> elements;

    CatalogResponsePhase(final SimpleResult result, final String errorMessage, final List<CatalogElement> elements) {
        super(result, errorMessage);
        this.elements = elements;
    }

    public List<CatalogElement> getElements() {
        return elements;
    }
}