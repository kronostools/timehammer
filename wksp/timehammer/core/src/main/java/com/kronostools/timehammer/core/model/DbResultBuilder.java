package com.kronostools.timehammer.core.model;

public abstract class DbResultBuilder<B> {
    protected String errorMessage;

    public B errorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
        return (B) this;
    }
}