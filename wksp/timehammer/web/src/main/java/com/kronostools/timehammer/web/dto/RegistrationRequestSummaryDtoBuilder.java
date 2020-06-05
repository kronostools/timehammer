package com.kronostools.timehammer.web.dto;

import java.util.ArrayList;
import java.util.List;

public class RegistrationRequestSummaryDtoBuilder {
    private String registrationRequestId;
    private List<ValidationErrorDto> validationErrors;

    public RegistrationRequestSummaryDtoBuilder() {
        validationErrors = new ArrayList<>();
    }

    public RegistrationRequestSummaryDtoBuilder registrationRequestId(final String workerInternalId) {
        this.registrationRequestId = workerInternalId;
        return this;
    }

    public void addValidationError(final ValidationErrorDto validationErrorDto) {
        validationErrors.add(validationErrorDto);
    }

    public RegistrationRequestSummaryDto build() {
        final RegistrationRequestSummaryDto rrsd = new RegistrationRequestSummaryDto();
        rrsd.setRegistrationRequestId(registrationRequestId);

        return rrsd;
    }
}