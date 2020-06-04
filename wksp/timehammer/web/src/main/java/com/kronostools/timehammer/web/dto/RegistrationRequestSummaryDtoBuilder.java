package com.kronostools.timehammer.web.dto;

public class RegistrationRequestSummaryDtoBuilder {
    private String workerInternalId;

    public RegistrationRequestSummaryDtoBuilder workerInternalId(final String workerInternalId) {
        this.workerInternalId = workerInternalId;
        return this;
    }

    public RegistrationRequestSummaryDto build() {
        final RegistrationRequestSummaryDto rrsd = new RegistrationRequestSummaryDto();
        rrsd.setWorkerInternalId(workerInternalId);

        return rrsd;
    }
}