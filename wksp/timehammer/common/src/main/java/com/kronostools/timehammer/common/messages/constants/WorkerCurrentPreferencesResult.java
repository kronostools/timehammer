package com.kronostools.timehammer.common.messages.constants;

public enum WorkerCurrentPreferencesResult implements PhaseResult {
    OK(null),
    UNREGISTERED_CHAT("There is no worker registered for the chat id"),
    UNEXPECTED_ERROR("Unexpected error");

    private final String defaultErrorMessage;

    WorkerCurrentPreferencesResult(final String defaultErrorMessage) {
        this.defaultErrorMessage = defaultErrorMessage;
    }

    @Override
    public String getDefaultErrorMessage() {
        return defaultErrorMessage;
    }
}