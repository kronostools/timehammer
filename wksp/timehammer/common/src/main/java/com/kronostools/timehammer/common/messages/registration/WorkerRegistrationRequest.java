package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.PlatformMessage;

import java.time.LocalDateTime;

@JsonDeserialize(builder = WorkerRegistrationRequestBuilder.class)
public class WorkerRegistrationRequest extends PlatformMessage {
    private String workerInternalId;
    private Company company;
    private String workerExternalId;
    private String workCity;
    private String workSsid;
    // TODO: add timetable
    private CheckRegistrationRequestPhase checkRegistrationRequestPhase;

    WorkerRegistrationRequest(final LocalDateTime generated, final String workerInternalId) {
        super(generated);
        this.workerInternalId = workerInternalId;
    }

    // TODO: revisar si hace falta este método
    // TODO: evaluar si crear otra clase que extienda de PlatformMessage y obligue a tener este método
    /*
    public boolean processedSuccessfully() {
        return Optional.ofNullable(saveHolidayPhase)
                .map(SaveHolidayPhase::isSuccessful)
                .orElse(false);
    }
    */

    public String getWorkerInternalId() {
        return workerInternalId;
    }

    public void setWorkerInternalId(String workerInternalId) {
        this.workerInternalId = workerInternalId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getWorkerExternalId() {
        return workerExternalId;
    }

    public void setWorkerExternalId(String workerExternalId) {
        this.workerExternalId = workerExternalId;
    }

    public String getWorkCity() {
        return workCity;
    }

    public void setWorkCity(String workCity) {
        this.workCity = workCity;
    }

    public String getWorkSsid() {
        return workSsid;
    }

    public void setWorkSsid(String workSsid) {
        this.workSsid = workSsid;
    }

    public CheckRegistrationRequestPhase getCheckRegistrationRequestPhase() {
        return checkRegistrationRequestPhase;
    }

    public void setCheckRegistrationRequestPhase(CheckRegistrationRequestPhase checkRegistrationRequestPhase) {
        this.checkRegistrationRequestPhase = checkRegistrationRequestPhase;
    }
}