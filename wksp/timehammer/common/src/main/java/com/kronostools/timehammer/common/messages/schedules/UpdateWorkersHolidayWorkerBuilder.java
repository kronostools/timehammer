package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.Company;

import java.time.LocalDate;
import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class UpdateWorkersHolidayWorkerBuilder extends ProcessableBatchScheduleMessageBuilder<UpdateWorkersHolidayWorkerBuilder> {
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
                    .generated(w.getGenerated())
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

    public UpdateWorkersHolidayWorkerBuilder credentialPhase(final CredentialPhase credentialPhase) {
        this.credentialPhase = credentialPhase;
        return this;
    }

    public UpdateWorkersHolidayWorkerBuilder checkHolidayPhase(final CheckHolidayPhase checkHolidayPhase) {
        this.checkHolidayPhase = checkHolidayPhase;
        return this;
    }

    public UpdateWorkersHolidayWorkerBuilder saveHolidayPhase(final SaveHolidayPhase saveHolidayPhase) {
        this.saveHolidayPhase = saveHolidayPhase;
        return this;
    }

    public UpdateWorkersHolidayWorker build() {
        final UpdateWorkersHolidayWorker result = new UpdateWorkersHolidayWorker(generated, name, executionId, batchSize);
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