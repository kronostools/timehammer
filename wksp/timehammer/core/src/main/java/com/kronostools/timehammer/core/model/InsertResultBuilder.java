package com.kronostools.timehammer.core.model;

import java.util.Optional;

public class InsertResultBuilder extends DbResultBuilder<InsertResultBuilder> {
    private Integer inserted;

    public static InsertResult copyAndBuild(final InsertResult insertResult) {
        return Optional.ofNullable(insertResult)
                .map(ir -> InsertResultBuilder.copy(ir).build())
                .orElse(null);
    }

    public static InsertResultBuilder copy(final InsertResult insertResult) {
        return Optional.ofNullable(insertResult)
                .map(ir -> new InsertResultBuilder()
                        .errorMessage(ir.getErrorMessage())
                        .inserted(ir.getInserted()))
                .orElse(null);
    }

    public InsertResultBuilder inserted(final Integer inserted) {
        this.inserted = inserted;
        return this;
    }

    public InsertResult build() {
        final InsertResult result = new InsertResult(errorMessage);
        result.setInserted(inserted);

        return result;
    }
}