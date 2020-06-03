package com.kronostools.timehammer.common.messages.constants;

public enum ValidateResult {
    VALID(true),
    INVALID(false);

    private final boolean successful;

    ValidateResult(final boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }
}