package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.Company;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class UpdateWorkersStatusWorkerBuilder extends ProcessableBatchScheduleMessageBuilder<UpdateWorkersStatusWorkerBuilder> {
    private String workerInternalId;
    private Company company;
    private String workerExternalId;
    private GetWorkerStatusPhase workerStatusPhase;
    private SaveWorkerStatusPhase saveWorkerStatusPhase;

    public static UpdateWorkersStatusWorker copyAndBuild(final UpdateWorkersStatusWorker worker) {
        return Optional.ofNullable(worker)
                .map(w -> UpdateWorkersStatusWorkerBuilder.copy(w).build())
                .orElse(null);
    }

    public static UpdateWorkersStatusWorkerBuilder copy(final UpdateWorkersStatusWorker worker) {
        return Optional.ofNullable(worker)
                .map(w -> new UpdateWorkersStatusWorkerBuilder()
                    .generated(w.getGenerated())
                    .executionId(w.getExecutionId())
                    .name(w.getName())
                    .batchSize(w.getBatchSize())
                    .workerInternalId(w.getWorkerInternalId())
                    .company(w.getCompany())
                    .workerExternalId(w.getWorkerExternalId())
                    .workerStatusPhase(GetWorkerStatusPhaseBuilder.copyAndBuild(w.getWorkerStatusPhase()))
                    .saveWorkerStatusPhase(SaveWorkerStatusPhaseBuilder.copyAndBuild(w.getSaveWorkerStatusPhase())))
                .orElse(null);
    }

    public UpdateWorkersStatusWorkerBuilder workerInternalId(final String workerInternalId) {
        this.workerInternalId = workerInternalId;
        return this;
    }

    public UpdateWorkersStatusWorkerBuilder company(final Company company) {
        this.company = company;
        return this;
    }

    public UpdateWorkersStatusWorkerBuilder workerExternalId(final String workerExternalId) {
        this.workerExternalId = workerExternalId;
        return this;
    }

    public UpdateWorkersStatusWorkerBuilder workerStatusPhase(final GetWorkerStatusPhase workerStatusPhase) {
        this.workerStatusPhase = workerStatusPhase;
        return this;
    }

    public UpdateWorkersStatusWorkerBuilder saveWorkerStatusPhase(final SaveWorkerStatusPhase saveWorkerStatusPhase) {
        this.saveWorkerStatusPhase = saveWorkerStatusPhase;
        return this;
    }

    public UpdateWorkersStatusWorker build() {
        final UpdateWorkersStatusWorker result = new UpdateWorkersStatusWorker(generated, name, executionId, batchSize);
        result.setWorkerInternalId(workerInternalId);
        result.setCompany(company);
        result.setWorkerExternalId(workerExternalId);
        result.setWorkerStatusPhase(workerStatusPhase);
        result.setSaveWorkerStatusPhase(saveWorkerStatusPhase);

        return result;
    }
}