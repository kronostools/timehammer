package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.Company;

import java.util.Optional;
import java.util.Set;

@JsonPOJOBuilder(withPrefix = "")
public class CheckWorkersStatusWorkerBuilder extends ProcessableBatchScheduleMessageBuilder<CheckWorkersStatusWorkerBuilder> {
    private String workerInternalId;
    private Company company;
    private String workerExternalId;
    private Set<String> chats;
    private GetWorkerStatusPhase workerStatusPhase;
    private SaveWorkerStatusPhase saveWorkerStatusPhase;

    public static CheckWorkersStatusWorker copyAndBuild(final CheckWorkersStatusWorker worker) {
        return Optional.ofNullable(worker)
                .map(w -> CheckWorkersStatusWorkerBuilder.copy(w).build())
                .orElse(null);
    }

    public static CheckWorkersStatusWorkerBuilder copy(final CheckWorkersStatusWorker worker) {
        return Optional.ofNullable(worker)
                .map(w -> new CheckWorkersStatusWorkerBuilder()
                    .generated(w.getGenerated())
                    .executionId(w.getExecutionId())
                    .name(w.getName())
                    .batchSize(w.getBatchSize())
                    .workerInternalId(w.getWorkerInternalId())
                    .company(w.getCompany())
                    .workerExternalId(w.getWorkerExternalId())
                    .chats(w.getChats())
                    .workerStatusPhase(GetWorkerStatusPhaseBuilder.copyAndBuild(w.getWorkerStatusPhase()))
                    .saveWorkerStatusPhase(SaveWorkerStatusPhaseBuilder.copyAndBuild(w.getSaveWorkerStatusPhase())))
                .orElse(null);
    }

    public CheckWorkersStatusWorkerBuilder workerInternalId(final String workerInternalId) {
        this.workerInternalId = workerInternalId;
        return this;
    }

    public CheckWorkersStatusWorkerBuilder company(final Company company) {
        this.company = company;
        return this;
    }

    public CheckWorkersStatusWorkerBuilder workerExternalId(final String workerExternalId) {
        this.workerExternalId = workerExternalId;
        return this;
    }

    public CheckWorkersStatusWorkerBuilder chats(final Set<String> chats) {
        this.chats = chats;
        return this;
    }

    public CheckWorkersStatusWorkerBuilder workerStatusPhase(final GetWorkerStatusPhase workerStatusPhase) {
        this.workerStatusPhase = workerStatusPhase;
        return this;
    }

    public CheckWorkersStatusWorkerBuilder saveWorkerStatusPhase(final SaveWorkerStatusPhase saveWorkerStatusPhase) {
        this.saveWorkerStatusPhase = saveWorkerStatusPhase;
        return this;
    }

    public CheckWorkersStatusWorker build() {
        final CheckWorkersStatusWorker result = new CheckWorkersStatusWorker(generated, name, executionId, batchSize);
        result.setWorkerInternalId(workerInternalId);
        result.setCompany(company);
        result.setWorkerExternalId(workerExternalId);
        result.setChats(chats);
        result.setWorkerStatusPhase(workerStatusPhase);
        result.setSaveWorkerStatusPhase(saveWorkerStatusPhase);

        return result;
    }
}