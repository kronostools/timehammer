package com.kronostools.timehammer.common.messages.constants;

public enum SaveWorkerResult implements PhaseResult {
    OK(true, null),
    INVALID(false,"Invalid input data"),
    KO(false,"Error");

    private final boolean successful;
    private final String defaultErrorMessage;

    SaveWorkerResult(final boolean successful, final String defaultErrorMessage) {
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