package com.kronostools.timehammer.core.model;

public class UpsertResult extends DbResult {
    private Long inserted;

    public static class Builder implements DbResultBuilder<UpsertResult> {
        private Long inserted;

        Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public Builder inserted(final Long inserted) {
            this.inserted = inserted;
            return this;
        }

        public UpsertResult build() {
            final UpsertResult result = new UpsertResult();
            result.setSuccessful(true);
            result.setInserted(inserted);

            return result;
        }

        @Override
        public UpsertResult buildUnsuccessful(final String errorMessage) {
            final UpsertResult result = new UpsertResult();
            result.setSuccessful(false);
            result.setErrorMessage(errorMessage);

            return result;
        }
    }

    public Long getInserted() {
        return inserted;
    }

    public void setInserted(Long inserted) {
        this.inserted = inserted;
    }
}