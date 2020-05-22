package com.kronostools.timehammer.core.model;

import java.util.List;

public abstract class MultipleResult<T> extends DbResult {
    protected List<T> result;

    MultipleResult(final String errorMessage) {
        super(errorMessage);
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}