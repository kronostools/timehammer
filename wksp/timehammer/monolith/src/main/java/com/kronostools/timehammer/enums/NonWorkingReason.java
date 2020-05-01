package com.kronostools.timehammer.enums;

public enum NonWorkingReason {
    NONE("NONE", ""),
    WEEKEND("WEEKEND", "Fin de semana"),
    WORKER_HOLIDAY("WORKER_HOLIDAY", "Vacaciones"),
    CITY_HOLIDAY("CITY_HOLIDAY", "Día festivo");

    private final String code;
    private final String text;

    NonWorkingReason(final String code, final String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}