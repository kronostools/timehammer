package com.kronostools.timehammer.comunytek.constants;

public enum ComunytekCachedLoginResult implements ComunytekResult {
    OK(true, null),
    MISSING(false, "Missing credentials"),
    INVALID(false, "Invalid credentials"),
    UNEXPECTED_ERROR(true, "Unexpected error");

    private final boolean successful;
    private final String defaultErrorMessage;

    ComunytekCachedLoginResult(final boolean successful, final String defaultErrorMessage) {
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
