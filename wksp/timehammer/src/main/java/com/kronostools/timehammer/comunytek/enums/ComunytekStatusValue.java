package com.kronostools.timehammer.comunytek.enums;

import java.util.stream.Stream;

public enum ComunytekStatusValue {
    INITIAL("", "Inicial"),
    UNKNOWN("", "Desconocido"),
    STARTED("E", "Entrada"),
    PAUSED("P", "Pausa"),
    RESUMED("R", "Reanudar"),
    ENDED("S", "Salida");

    private final String code;
    private final String text;

    ComunytekStatusValue(final String code, final String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static ComunytekStatusValue fromCode(final String code) {
        return Stream.of(ComunytekStatusValue.values())
                .filter(s -> s.getCode().equals(code))
                .findFirst()
                .orElse(ComunytekStatusValue.UNKNOWN);
    }
}