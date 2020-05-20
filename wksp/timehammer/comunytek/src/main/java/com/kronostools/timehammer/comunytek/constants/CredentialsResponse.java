package com.kronostools.timehammer.comunytek.constants;

public enum CredentialsResponse {
    OK(true),
    NOT_FOUND(false),
    EXPIRED(false);

    private final boolean successful;

    CredentialsResponse(final boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
