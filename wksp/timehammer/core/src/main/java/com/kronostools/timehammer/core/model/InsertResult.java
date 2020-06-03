package com.kronostools.timehammer.core.model;

public class InsertResult extends DbResult {
    private Integer inserted;

    InsertResult(final String errorMessage) {
        super(errorMessage);
    }

    public Integer getInserted() {
        return inserted;
    }

    public void setInserted(Integer inserted) {
        this.inserted = inserted;
    }
}