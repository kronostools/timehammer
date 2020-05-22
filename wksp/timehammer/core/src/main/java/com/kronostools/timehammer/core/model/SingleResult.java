package com.kronostools.timehammer.core.model;

public abstract class SingleResult<T> extends DbResult {
    protected T result;

    SingleResult(final String errorMessage) {
        super(errorMessage);
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}