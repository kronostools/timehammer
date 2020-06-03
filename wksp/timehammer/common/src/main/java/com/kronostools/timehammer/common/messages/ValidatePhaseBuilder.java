package com.kronostools.timehammer.common.messages;

import com.kronostools.timehammer.common.messages.constants.ValidatePhaseResult;

import java.util.ArrayList;
import java.util.List;

public abstract class ValidatePhaseBuilder<R extends Enum<R> & ValidatePhaseResult, V, B, C> {
    protected R result;
    protected V validatedForm;
    protected List<ValidationError> validationErrors;

    public B result(final R result) {
        this.result = result;
        return (B) this;
    }

    public B validatedForm(final V validatedForm) {
        this.validatedForm = validatedForm;
        return (B) this;
    }

    public B validationErrors(final List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
        return (B) this;
    }

    public B addValidationError(final ValidationError validationError) {
        if (validationErrors == null) {
            validationErrors = new ArrayList<>();
        }

        validationErrors.add(validationError);
        return (B) this;
    }

    public abstract C build();
}