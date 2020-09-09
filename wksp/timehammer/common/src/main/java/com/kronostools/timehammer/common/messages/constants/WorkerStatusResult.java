package com.kronostools.timehammer.common.messages.constants;

public enum WorkerStatusResult implements PhaseResult {
    OK(true, null),
    MISSING_OR_INVALID_CREDENTIALS(false,"Missing or invalid credentials"),
    IGNORED_NOT_WORKING_TODAY(false, "Status was not updated because worker does not work today"),
    KO(false,"Error");

    private final boolean successful;
    private final String defaultErrorMessage;

    WorkerStatusResult(final boolean successful, final String defaultErrorMessage) {
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