package com.kronostools.timehammer.core.model;

public abstract class SingleResultBuilder<B, T> extends DbResultBuilder<B> {
    protected T result;

    public B result(final T result) {
        this.result = result;
        return (B) this;
    }
}