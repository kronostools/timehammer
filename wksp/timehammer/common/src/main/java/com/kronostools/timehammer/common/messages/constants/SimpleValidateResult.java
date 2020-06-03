package com.kronostools.timehammer.common.messages.constants;

public enum SimpleValidateResult implements ValidatePhaseResult {
    VALID(true),
    INVALID(false);

    private final boolean successful;

    SimpleValidateResult(final boolean successful) {
        this.successful = successful;
    }

    @Override
    public boolean isSuccessful() {
        return successful;
    }
}