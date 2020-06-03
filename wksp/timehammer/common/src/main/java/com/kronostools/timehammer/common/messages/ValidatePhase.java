package com.kronostools.timehammer.common.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kronostools.timehammer.common.messages.constants.ValidatePhaseResult;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ValidatePhase<R extends Enum<R> & ValidatePhaseResult, V> {
    protected final R result;
    protected final V validatedForm;
    protected final List<ValidationError> validationErrors;

    protected ValidatePhase(final R result, final V validatedForm, final List<ValidationError> validationErrors) {
        this.result = result;
        this.validatedForm = validatedForm;
        this.validationErrors = validationErrors != null ? validationErrors : Collections.emptyList();
    }

    @JsonIgnore
    public boolean isSuccessful() {
        return result.isSuccessful();
    }

    @JsonIgnore
    public boolean isNotSuccessful() {
        return !result.isSuccessful();
    }

    @JsonIgnore
    public List<ValidationError> getGlobalErrors() {
        return validationErrors.stream()
                .filter(ve -> ve.getFieldName() == null)
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public List<ValidationError> getFieldErrors() {
        return validationErrors.stream()
                .filter(ve -> ve.getFieldName() != null)
                .collect(Collectors.toList());
    }

    public R getResult() {
        return result;
    }

    public V getValidatedForm() {
        return validatedForm;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }
}