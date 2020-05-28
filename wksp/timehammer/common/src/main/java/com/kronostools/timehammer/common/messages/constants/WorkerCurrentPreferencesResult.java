package com.kronostools.timehammer.common.messages.constants;

public enum WorkerCurrentPreferencesResult implements PhaseResult {
    OK(true,null),
    UNREGISTERED_CHAT(false,"There is no worker registered for the chat id"),
    UNEXPECTED_ERROR(false,"Unexpected error");

    private final boolean successful;
    private final String defaultErrorMessage;

    WorkerCurrentPreferencesResult(final boolean successful, final String defaultErrorMessage) {
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