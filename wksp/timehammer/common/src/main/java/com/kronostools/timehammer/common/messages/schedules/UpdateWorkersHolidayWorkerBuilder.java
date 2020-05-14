package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.PlatformMessageBuilder;

import java.time.LocalDate;
import java.util.UUID;

@JsonPOJOBuilder(withPrefix = "")
public class UpdateWorkersHolidayWorkerBuilder extends PlatformMessageBuilder<UpdateWorkersHolidayWorkerBuilder> {
    private UUID executionId;
    private String name;
    private Integer batchSize;

    private String workerInternalId;
    private Company company;
    private String workerExternalId;
    private CredentialPhase credentialPhase;
    private LocalDate holidayCandidate;
    private CheckHolidayPhase checkHolidayPhase;
    private SaveHolidayPhase saveHolidayPhase;

    public static UpdateWorkersHolidayWorkerBuilder copy(final UpdateWorkersHolidayWorker worker) {
        return new UpdateWorkersHolidayWorkerBuilder()
                .timestamp(worker.getTimestamp())
                .executionId(worker.getExecutionId())
                .name(worker.getName())
                .batchSize(worker.getBatchSize())
                .holidayCandidate(worker.getHolidayCandidate())
                .workerInternalId(worker.getWorkerInternalId())
                .company(worker.getCompany())
                .workerExternalId(worker.getWorkerExternalId())
                .credentialPhase(CredentialPhaseBuilder.copy(worker.getCredentialPhase()).build())
                .checkHolidayPhase(CheckHolidayPhaseBuilder.copy(worker.getCheckHolidayPhase()).build())
                .saveHolidayPhase(SaveHolidayPhaseBuilder.copy(worker.getSaveHolidayPhase()).build());
    }

    public UpdateWorkersHolidayWorkerBuilder executionId(final UUID executionId) {
        this.executionId = executionId;
        return this;
    }

    public UpdateWorkersHolidayWorkerBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public UpdateWorkersHolidayWorkerBuilder batchSize(final Integer batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public UpdateWorkersHolidayWorkerBuilder holidayCandidate(final LocalDate holidayCandidate) {
        this.holidayCandidate = holidayCandidate;
        return this;
    }

    public UpdateWorkersHolidayWorkerBuilder workerInternalId(final String workerInternalId) {
        this.workerInternalId = workerInternalId;
        return this;
    }

    public UpdateWorkersHolidayWorkerBuilder company(final Company company) {
        this.company = company;
        return this;
    }

    public UpdateWorkersHolidayWorkerBuilder workerExternalId(final String workerExternalId) {
        this.workerExternalId = workerExternalId;
        return this;
    }

    public UpdateWorkersHolidayWorkerBuilder credentialPhase(final CredentialPhase credentialResult) {
        this.credentialPhase = credentialResult;
        return this;
    }

    public UpdateWorkersHolidayWorkerBuilder checkHolidayPhase(final CheckHolidayPhase checkHolidayResult) {
        this.checkHolidayPhase = checkHolidayResult;
        return this;
    }

    public UpdateWorkersHolidayWorkerBuilder saveHolidayPhase(final SaveHolidayPhase saveHolidayResult) {
        this.saveHolidayPhase = saveHolidayResult;
        return this;
    }

    public UpdateWorkersHolidayWorker build() {
        final UpdateWorkersHolidayWorker result = new UpdateWorkersHolidayWorker(timestamp);
        result.setExecutionId(executionId);
        result.setName(name);
        result.setBatchSize(batchSize);
        result.setHolidayCandidate(holidayCandidate);
        result.setWorkerInternalId(workerInternalId);
        result.setCompany(company);
        result.setWorkerExternalId(workerExternalId);
        result.setCredentialPhase(credentialPhase);
        result.setCheckHolidayPhase(checkHolidayPhase);
        result.setSaveHolidayPhase(saveHolidayPhase);

        return result;
    }
}