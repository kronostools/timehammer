package com.kronostools.timehammer.common.constants;

public enum CatalogType {
    COMPANY("Compañía"),
    CITY("Ciudad");

    private final String label;

    CatalogType(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}