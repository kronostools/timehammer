package com.kronostools.timehammer.common.messages.catalog;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonPOJOBuilder(withPrefix = "")
public class CatalogElementBuilder {
    private String code;
    private String label;

    public CatalogElementBuilder code(final String code) {
        this.code = code;
        return this;
    }

    public CatalogElementBuilder label(final String label) {
        this.label = label;
        return this;
    }

    public CatalogElement build() {
        return new CatalogElement(code, label);
    }
}