package com.kronostools.timehammer.web.dto;

import com.kronostools.timehammer.common.messages.ValidationError;

import java.util.Optional;

public class ValidationErrorDtoBuilder {
    private String fieldName;
    private String errorMessage;

    public static ValidationErrorDto copyAndBuild(final ValidationError validationError) {
        return Optional.ofNullable(validationError)
                .map(ve -> copy(ve).build())
                .orElse(null);
    }

    public static ValidationErrorDtoBuilder copy(final ValidationError validationError) {
        return Optional.ofNullable(validationError)
                .map(ve -> new ValidationErrorDtoBuilder()
                        .fieldName(ve.getFieldName())
                        .errorMessage(ve.getErrorMessage()))
                .orElse(null);
    }

    public ValidationErrorDtoBuilder fieldName(final String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public ValidationErrorDtoBuilder errorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public ValidationErrorDto build() {
        final ValidationErrorDto ve = new ValidationErrorDto();
        ve.setFieldName(fieldName);
        ve.setErrorMessage(errorMessage);

        return ve;
    }
}