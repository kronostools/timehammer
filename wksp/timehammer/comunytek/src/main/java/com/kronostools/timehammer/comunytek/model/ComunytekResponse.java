package com.kronostools.timehammer.comunytek.model;

public class ComunytekResponse {
    protected final boolean successful;
    protected final String errorMessage;

    ComunytekResponse(final boolean successful, final String errorMessage) {
        this.successful = successful;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}