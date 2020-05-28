package com.kronostools.timehammer.common.messages.constants;

public enum WorkerCredentialsResult implements PhaseResult {
    OK(true, null),
    INVALID(false,"Credentials are invalid"),
    UNEXPECTED_ERROR(false,"Unexpected error");

    private final boolean successful;
    private final String defaultErrorMessage;

    WorkerCredentialsResult(final boolean successful, final String defaultErrorMessage) {
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