package com.kronostools.timehammer.common.messages.constants;

public enum SimpleResult implements PhaseResult {
    OK(null),
    UNEXPECTED_ERROR("Unexpected error");

    private final String defaultErrorMessage;

    SimpleResult(final String defaultErrorMessage) {
        this.defaultErrorMessage = defaultErrorMessage;
    }

    @Override
    public String getDefaultErrorMessage() {
        return defaultErrorMessage;
    }
}