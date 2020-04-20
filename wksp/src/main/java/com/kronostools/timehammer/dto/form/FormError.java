package com.kronostools.timehammer.dto.form;

public class FormError {
    private final String fieldId;
    private final String errorMessage;

    public FormError(final String fieldId, final String errorMessage) {
        this.fieldId = fieldId;
        this.errorMessage = errorMessage;
    }

    public FormError(final String errorMessage) {
        this.fieldId = "";
        this.errorMessage = errorMessage;
    }

    public String getFieldId() {
        return fieldId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "FormError{" +
                "fieldId='" + fieldId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}