package com.kronostools.timehammer.dto;

import com.kronostools.timehammer.dto.form.FormError;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FormResponse {
    private final Boolean processedSuccessfully;
    private final Set<FormError> formErrors;

    FormResponse(final Boolean processedSuccessfully, final Set<FormError> formErrors) {
        this.processedSuccessfully = processedSuccessfully;
        this.formErrors = formErrors;
    }

    public Boolean getProcessedSuccessfully() {
        return processedSuccessfully;
    }

    public Set<FormError> getFormErrors() {
        return formErrors;
    }

    public static class FormResponseBuilder {
        private Set<FormError> formErrors = new HashSet<>();

        public void addFormError(final String errorMessage) {
            addFormError(new FormError(errorMessage));
        }

        public void addFieldError(final String fieldName, final String errorMessage) {
            formErrors.add(new FormError(fieldName, errorMessage));
        }

        public void addFormError(final FormError formError) {
            formErrors.add(formError);
        }

        public FormResponse build() {
            return new FormResponse(formErrors.isEmpty(), formErrors);
        }
    }

    @Override
    public String toString() {
        return "FormResponse{" +
                "processedSuccessfully=" + processedSuccessfully +
                ", formErrors=[" + formErrors.stream().map(FormError::toString).collect(Collectors.joining(", ")) +
                "]}";
    }
}