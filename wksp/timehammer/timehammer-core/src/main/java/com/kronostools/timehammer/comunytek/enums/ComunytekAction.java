package com.kronostools.timehammer.comunytek.enums;

import java.util.stream.Stream;

public enum ComunytekAction {
    START("E", "Entrada", ""),
    LUNCH_PAUSE("P", "Pausa", "Comida"),
    LUNCH_RESUME("R", "Reanudar", ""),
    END("S", "Salida", "");

    private final String code;
    private final String description;
    private final String comment;

    ComunytekAction(final String code, final String description, final String comment) {
        this.code = code;
        this.description = description;
        this.comment = comment;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getComment() {
        return comment;
    }

    public static ComunytekAction fromCode(final String code) {
        return Stream.of(ComunytekAction.values())
                .filter(a -> a.getCode().equals(code))
                .findFirst()
                .get();
    }
}