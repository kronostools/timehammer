package com.kronostools.timehammer.common.messages;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = ValidationErrorBuilder.class)
public class ValidationError {
    private final String fieldName;
    private final String errorMessage;

    public ValidationError(final String fieldName, final String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}