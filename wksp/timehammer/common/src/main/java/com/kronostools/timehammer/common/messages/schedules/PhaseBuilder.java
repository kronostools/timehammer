package com.kronostools.timehammer.common.messages.schedules;

public abstract class PhaseBuilder<B> {
    protected String errorMessage;

    public B errorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
        return (B) this;
    }
}