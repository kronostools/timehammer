package com.kronostools.timehammer.common.messages.catalog;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.PhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

import java.util.Optional;
import java.util.Set;

@JsonPOJOBuilder(withPrefix = "")
public class CatalogResponsePhaseBuilder extends PhaseBuilder<SimpleResult, CatalogResponsePhaseBuilder> {
    private Set<CatalogElement> elements;

    public static CatalogResponsePhase copyAndBuild(final CatalogResponsePhase catalogResponsePhase) {
        return Optional.ofNullable(catalogResponsePhase)
                .map(crp -> CatalogResponsePhaseBuilder.copy(crp).build())
                .orElse(null);
    }

    public static CatalogResponsePhaseBuilder copy(final CatalogResponsePhase catalogResponsePhase) {
        return Optional.ofNullable(catalogResponsePhase)
                .map(crp -> new CatalogResponsePhaseBuilder()
                        .result(crp.getResult())
                        .errorMessage(crp.getErrorMessage())
                        .elements(crp.getElements()))
                .orElse(null);
    }

    public CatalogResponsePhaseBuilder elements(final Set<CatalogElement> elements) {
        this.elements = elements;
        return this;
    }

    public CatalogResponsePhase build() {
        return new CatalogResponsePhase(result, errorMessage, elements);
    }
}