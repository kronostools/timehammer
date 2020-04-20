package com.kronostools.timehammer.comunytek.enums;

import java.util.stream.Stream;

public enum ComunytekStatusValue {
    INITIAL(""), UNKNOWN(""), STARTED("E"), PAUSED("P"), RESUMED("R"), ENDED("S");

    private final String code;

    ComunytekStatusValue(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ComunytekStatusValue fromString(final String code) {
        return Stream.of(ComunytekStatusValue.values())
                .filter(s -> s.getCode().equals(code))
                .findFirst()
                .orElse(ComunytekStatusValue.UNKNOWN);
    }
}