package com.kronostools.timehammer.dto;

import java.io.Serializable;

public class CompanyDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String code;
    private final String name;

    public CompanyDto(final String code, final String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}