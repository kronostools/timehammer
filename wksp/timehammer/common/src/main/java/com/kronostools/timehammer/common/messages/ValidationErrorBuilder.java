package com.kronostools.timehammer.common.messages;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonPOJOBuilder(withPrefix = "")
public class ValidationErrorBuilder {
    private String fieldName;
    private String errorMessage;

    public ValidationErrorBuilder fieldName(final String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public ValidationErrorBuilder errorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public ValidationError build() {
        return new ValidationError(fieldName, errorMessage);
    }
}