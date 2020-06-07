package com.kronostools.timehammer.common.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kronostools.timehammer.common.messages.constants.ValidateResult;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ValidatePhase {
    protected final ValidateResult result;
    protected final List<ValidationError> validationErrors;

    protected ValidatePhase(final ValidateResult result, final List<ValidationError> validationErrors) {
        this.result = result;
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

    @JsonIgnore
    public ValidateResult getResult() {
        return result;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }
}