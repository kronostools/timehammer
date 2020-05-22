package com.kronostools.timehammer.common.messages;

import com.kronostools.timehammer.common.messages.constants.PhaseResult;

public abstract class PhaseBuilder<R extends Enum<R> & PhaseResult, B> {
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