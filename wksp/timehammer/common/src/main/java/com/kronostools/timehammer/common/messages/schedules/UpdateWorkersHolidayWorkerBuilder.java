package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.PlatformMessageBuilder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@JsonPOJOBuilder(withPrefix = "")
public class UpdateWorkersHolidayWorkerBuilder extends PlatformMessageBuilder<UpdateWorkersHolidayWorkerBuilder> {
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

    public static UpdateWorkersHolidayWorker copyAndBuild(final UpdateWorkersHolidayWorker worker) {
        return Optional.ofNullable(worker)
                .map(w -> UpdateWorkersHolidayWorkerBuilder.copy(w).build())
                .orElse(null);
    }

    public static UpdateWorkersHolidayWorkerBuilder copy(final UpdateWorkersHolidayWorker worker) {
        return Optional.ofNullable(worker)
                .map(w -> new UpdateWorkersHolidayWorkerBuilder()
                    .timestamp(w.getTimestamp())
                    .executionId(w.getExecutionId())
                    .name(w.getName())
                    .batchSize(w.getBatchSize())
                    .holidayCandidate(w.getHolidayCandidate())
                    .workerInternalId(w.getWorkerInternalId())
                    .company(w.getCompany())
                    .workerExternalId(w.getWorkerExternalId())
                    .credentialPhase(CredentialPhaseBuilder.copyAndBuild(w.getCredentialPhase()))
                    .checkHolidayPhase(CheckHolidayPhaseBuilder.copyAndBuild(w.getCheckHolidayPhase()))
                    .saveHolidayPhase(SaveHolidayPhaseBuilder.copyAndBuild(w.getSaveHolidayPhase())))
                .orElse(null);
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