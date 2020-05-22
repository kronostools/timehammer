package com.kronostools.timehammer.core.model;

import java.util.List;

public abstract class MultipleResultBuilder<B, T> extends DbResultBuilder<B> {
    protected List<T> result;

    public B result(final List<T> result) {
        this.result = result;
        return (B) this;
    }
}