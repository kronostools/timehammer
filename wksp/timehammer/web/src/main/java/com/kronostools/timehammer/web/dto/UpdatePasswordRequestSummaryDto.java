package com.kronostools.timehammer.web.dto;

import java.util.List;

public class UpdatePasswordRequestSummaryDto extends Dto {
    private String requestId;
    private List<ValidationErrorDto> validationErrors;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<ValidationErrorDto> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationErrorDto> validationErrors) {
        this.validationErrors = validationErrors;
    }
}