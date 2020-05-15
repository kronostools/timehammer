package com.kronostools.timehammer.core.model;

public class UpsertResult extends DbResult {
    private Integer inserted;

    UpsertResult(final String errorMessage) {
        super(errorMessage);
    }

    public Integer getInserted() {
        return inserted;
    }

    public void setInserted(Integer inserted) {
        this.inserted = inserted;
    }
}