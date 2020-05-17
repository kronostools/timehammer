package com.kronostools.timehammer.core.model;

public class DeleteResult extends DbResult {
    private Integer deleted;

    DeleteResult(final String errorMessage) {
        super(errorMessage);
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}