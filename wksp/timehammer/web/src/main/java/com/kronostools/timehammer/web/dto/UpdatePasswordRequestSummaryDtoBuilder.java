package com.kronostools.timehammer.web.dto;

import java.util.ArrayList;
import java.util.List;

public class UpdatePasswordRequestSummaryDtoBuilder {
    private String requestId;
    private List<ValidationErrorDto> validationErrors;

    public UpdatePasswordRequestSummaryDtoBuilder() {
        validationErrors = new ArrayList<>();
    }

    public UpdatePasswordRequestSummaryDtoBuilder registrationRequestId(final String workerInternalId) {
        this.requestId = workerInternalId;
        return this;
    }

    public void addValidationError(final ValidationErrorDto validationErrorDto) {
        validationErrors.add(validationErrorDto);
    }

    public UpdatePasswordRequestSummaryDto build() {
        final UpdatePasswordRequestSummaryDto rrsd = new UpdatePasswordRequestSummaryDto();
        rrsd.setRequestId(requestId);
        rrsd.setValidationErrors(validationErrors);

        return rrsd;
    }
}