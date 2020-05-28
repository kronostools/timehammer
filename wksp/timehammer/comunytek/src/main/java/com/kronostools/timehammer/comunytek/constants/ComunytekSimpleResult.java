package com.kronostools.timehammer.comunytek.constants;

public enum ComunytekSimpleResult implements ComunytekResult {
    OK(true, null),
    KO(true, "Error");

    private final boolean successful;
    private final String defaultErrorMessage;

    ComunytekSimpleResult(final boolean successful, final String defaultErrorMessage) {
        this.successful = successful;
        this.defaultErrorMessage = defaultErrorMessage;
    }

    @Override
    public boolean isSuccessful() {
        return successful;
    }

    @Override
    public String getDefaultErrorMessage() {
        return defaultErrorMessage;
    }
}