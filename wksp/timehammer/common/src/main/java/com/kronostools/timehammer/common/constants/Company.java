package com.kronostools.timehammer.common.constants;

import java.util.stream.Stream;

public enum Company {
    UNKNOWN("UNKNOWN", "", Boolean.FALSE),
    COMUNYTEK("COMUNYTEK", "Comunytek", Boolean.TRUE);

    private final String code;
    private final String text;
    private final Boolean selectionable;

    Company(final String code, final String text, final Boolean selectionable) {
        this.code = code;
        this.text = text;
        this.selectionable = selectionable;
    }

    public static Company fromCode(final String code) {
        return Stream.of(Company.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElse(UNKNOWN);
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public Boolean isSelectionable() {
        return selectionable;
    }
}