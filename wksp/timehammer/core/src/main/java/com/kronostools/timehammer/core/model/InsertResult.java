package com.kronostools.timehammer.core.model;

public class InsertResult extends DbResult {
    private int inserted;

    InsertResult(final String errorMessage) {
        super(errorMessage);
    }

    public int getInserted() {
        return inserted;
    }

    public void setInserted(int inserted) {
        this.inserted = inserted;
    }
}