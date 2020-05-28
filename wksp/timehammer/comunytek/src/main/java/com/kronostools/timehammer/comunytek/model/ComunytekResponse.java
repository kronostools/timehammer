package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.comunytek.constants.ComunytekResult;

public abstract class ComunytekResponse<R extends Enum<R> & ComunytekResult> {
    protected R result;
    protected String errorMessage;

    ComunytekResponse(final R result, final String errorMessage) {
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccessful() {
        return result.isSuccessful();
    }

    public R getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}