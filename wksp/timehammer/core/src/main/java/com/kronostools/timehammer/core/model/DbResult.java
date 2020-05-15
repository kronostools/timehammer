package com.kronostools.timehammer.core.model;

public abstract class DbResult {
    protected boolean successful;
    protected String errorMessage;

    DbResult(final String errorMessage) {
        this.successful = errorMessage == null;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public boolean isNotSuccessful() {
        return !successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
