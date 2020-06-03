package com.kronostools.timehammer.common.messages;

import java.util.List;

public abstract class ValidatePhaseBuilder<B, C> {
    protected List<ValidationError> validationErrors;

    public B validationErrors(final List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
        return (B) this;
    }

    public abstract C build();
}