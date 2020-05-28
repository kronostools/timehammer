package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.comunytek.constants.ComunytekResult;

public abstract class ComunytekResponseBuilder<R extends Enum<R> & ComunytekResult, B> {
    protected R result;
    protected String errorMessage;

    public B result(final R result) {
        this.result = result;
        return (B) this;
    }

    public B errorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
        return (B) this;
    }
}