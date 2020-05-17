package com.kronostools.timehammer.core.model;

import java.util.Optional;

public class DeleteResultBuilder extends DbResultBuilder<DeleteResultBuilder> {
    private Integer deleted;

    public static DeleteResult copyAndBuild(final DeleteResult deleteResult) {
        return Optional.ofNullable(deleteResult)
                .map(dr -> DeleteResultBuilder.copy(dr).build())
                .orElse(null);
    }

    public static DeleteResultBuilder copy(final DeleteResult deleteResult) {
        return Optional.ofNullable(deleteResult)
                .map(dr -> new DeleteResultBuilder()
                        .errorMessage(dr.getErrorMessage())
                        .deleted(dr.getDeleted()))
                .orElse(null);
    }

    public DeleteResultBuilder deleted(final Integer inserted) {
        this.deleted = inserted;
        return this;
    }

    public DeleteResult build() {
        final DeleteResult result = new DeleteResult(errorMessage);
        result.setDeleted(deleted);

        return result;
    }
}