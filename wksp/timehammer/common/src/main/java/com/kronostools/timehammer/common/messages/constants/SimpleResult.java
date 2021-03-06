package com.kronostools.timehammer.common.messages.constants;

public enum SimpleResult implements PhaseResult {
    OK(true,null),
    KO(false,"Error");

    private final boolean successful;
    private final String defaultErrorMessage;

    SimpleResult(final boolean successful, final String defaultErrorMessage) {
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