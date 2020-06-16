package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.constants.Company;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@JsonDeserialize(builder = UpdateWorkersStatusWorkerBuilder.class)
public class UpdateWorkersStatusWorker extends ProcessableBatchScheduleMessage {
    private String workerInternalId;
    private Company company;
    private String workerExternalId;
    private GetWorkerStatusPhase workerStatusPhase;
    private SaveWorkerStatusPhase saveWorkerStatusPhase;

    UpdateWorkersStatusWorker(final LocalDateTime timestamp, final String name, final UUID executionId, final Integer batchSize) {
        super(timestamp, name, executionId, batchSize);
    }

    public boolean processedSuccessfully() {
        return Optional.ofNullable(saveWorkerStatusPhase)
                .map(SaveWorkerStatusPhase::isSuccessful)
                .orElse(false);
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

    public GetWorkerStatusPhase getWorkerStatusPhase() {
        return workerStatusPhase;
    }

    public void setWorkerStatusPhase(GetWorkerStatusPhase workerStatusPhase) {
        this.workerStatusPhase = workerStatusPhase;
    }

    public SaveWorkerStatusPhase getSaveWorkerStatusPhase() {
        return saveWorkerStatusPhase;
    }

    public void setSaveWorkerStatusPhase(SaveWorkerStatusPhase saveWorkerStatusPhase) {
        this.saveWorkerStatusPhase = saveWorkerStatusPhase;
    }
}