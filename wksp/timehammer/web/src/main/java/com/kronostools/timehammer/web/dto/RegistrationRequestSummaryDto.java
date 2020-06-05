package com.kronostools.timehammer.web.dto;

import java.util.List;

public class RegistrationRequestSummaryDto extends Dto {
    private String registrationRequestId;
    private List<ValidationErrorDto> validationErrors;

    public String getRegistrationRequestId() {
        return registrationRequestId;
    }

    public void setRegistrationRequestId(String registrationRequestId) {
        this.registrationRequestId = registrationRequestId;
    }

    public List<ValidationErrorDto> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationErrorDto> validationErrors) {
        this.validationErrors = validationErrors;
    }
}