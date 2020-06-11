package com.kronostools.timehammer.web.dto;

public class CatalogElementDto extends Dto {
    private String code;
    private String label;

    public CatalogElementDto() {}

    public CatalogElementDto(final String code, final String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}