package com.kronostools.timehammer.web.dto;

public class RegistrationRequestSummaryDto extends Dto {
    private String workerInternalId;

    public String getWorkerInternalId() {
        return workerInternalId;
    }

    public void setWorkerInternalId(String workerInternalId) {
        this.workerInternalId = workerInternalId;
    }
}