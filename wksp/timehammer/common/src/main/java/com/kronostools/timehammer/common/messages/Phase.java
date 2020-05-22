package com.kronostools.timehammer.common.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kronostools.timehammer.common.messages.constants.PhaseResult;

public abstract class Phase<R extends Enum<R> & PhaseResult> {
    protected R result;
    protected String errorMessage;
    protected boolean successful;

    protected Phase(final R result, final String errorMessage) {
        this.result = result;
        this.errorMessage = errorMessage;
        this.successful = result == null;
    }

    @JsonIgnore
    public boolean isSuccessful() {
        return successful;
    }

    @JsonIgnore
    public boolean isNotSuccessful() {
        return !successful;
    }

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage != null ? errorMessage : result != null ? result.getDefaultErrorMessage() : null;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}