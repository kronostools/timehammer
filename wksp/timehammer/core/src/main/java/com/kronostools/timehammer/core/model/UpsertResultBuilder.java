package com.kronostools.timehammer.core.model;

import java.util.Optional;

public class UpsertResultBuilder extends DbResultBuilder<UpsertResultBuilder> {
    private Integer inserted;

    public static UpsertResult copyAndBuild(final UpsertResult upsertResult) {
        return Optional.ofNullable(upsertResult)
                .map(ur -> UpsertResultBuilder.copy(ur).build())
                .orElse(null);
    }

    public static UpsertResultBuilder copy(final UpsertResult upsertResult) {
        return Optional.ofNullable(upsertResult)
                .map(ur -> new UpsertResultBuilder()
                        .errorMessage(ur.getErrorMessage())
                        .inserted(ur.getInserted()))
                .orElse(null);
    }

    public UpsertResultBuilder inserted(final Integer inserted) {
        this.inserted = inserted;
        return this;
    }

    public UpsertResult build() {
        final UpsertResult result = new UpsertResult(errorMessage);
        result.setInserted(inserted);

        return result;
    }
}