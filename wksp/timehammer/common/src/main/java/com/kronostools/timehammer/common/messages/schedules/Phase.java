package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Phase {
    protected boolean successful;
    protected String errorMessage;

    Phase(final String errorMessage) {
        this.successful = errorMessage == null;
        this.errorMessage = errorMessage;
    }

    @JsonIgnore
    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    @JsonIgnore
    public boolean isNotSuccessful() {
        return !successful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}