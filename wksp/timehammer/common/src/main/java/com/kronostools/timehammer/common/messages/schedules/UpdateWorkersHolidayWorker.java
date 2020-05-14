package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.PlatformMessage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonDeserialize(builder = UpdateWorkersHolidayWorkerBuilder.class)
public class UpdateWorkersHolidayWorker extends PlatformMessage {
    private UUID executionId;
    private String name;
    private Integer batchSize;

    private LocalDate holidayCandidate;
    private String workerInternalId;
    private Company company;
    private String workerExternalId;
    private CredentialPhase credentialPhase;
    private CheckHolidayPhase checkHolidayPhase;
    private SaveHolidayPhase saveHolidayPhase;

    UpdateWorkersHolidayWorker(final LocalDateTime timestamp) {
        super(timestamp);
    }

    public UUID getExecutionId() {
        return executionId;
    }

    public void setExecutionId(UUID executionId) {
        this.executionId = executionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public LocalDate getHolidayCandidate() {
        return holidayCandidate;
    }

    public void setHolidayCandidate(LocalDate holidayCandidate) {
        this.holidayCandidate = holidayCandidate;
    }

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

    public CredentialPhase getCredentialPhase() {
        return credentialPhase;
    }

    public void setCredentialPhase(CredentialPhase credentialPhase) {
        this.credentialPhase = credentialPhase;
    }

    public CheckHolidayPhase getCheckHolidayPhase() {
        return checkHolidayPhase;
    }

    public void setCheckHolidayPhase(CheckHolidayPhase checkHolidayPhase) {
        this.checkHolidayPhase = checkHolidayPhase;
    }

    public SaveHolidayPhase getSaveHolidayPhase() {
        return saveHolidayPhase;
    }

    public void setSaveHolidayPhase(SaveHolidayPhase saveHolidayPhase) {
        this.saveHolidayPhase = saveHolidayPhase;
    }
}