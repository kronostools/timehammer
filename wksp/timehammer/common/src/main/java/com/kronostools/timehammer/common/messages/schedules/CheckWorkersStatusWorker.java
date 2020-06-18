package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.schedules.model.WorkerCurrentPreferences;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@JsonDeserialize(builder = CheckWorkersStatusWorkerBuilder.class)
public class CheckWorkersStatusWorker extends ProcessableBatchScheduleMessage {
    private WorkerCurrentPreferences workerCurrentPreferences;
    private GetWorkerStatusPhase workerStatusPhase;
    private SaveWorkerStatusPhase saveWorkerStatusPhase;

    CheckWorkersStatusWorker(final LocalDateTime timestamp, final String name, final UUID executionId, final Integer batchSize) {
        super(timestamp, name, executionId, batchSize);
    }

    public boolean processedSuccessfully() {
        return Optional.ofNullable(saveWorkerStatusPhase)
                .map(SaveWorkerStatusPhase::isSuccessful)
                .orElse(false);
    }

    public WorkerCurrentPreferences getWorkerCurrentPreferences() {
        return workerCurrentPreferences;
    }

    public void setWorkerCurrentPreferences(WorkerCurrentPreferences workerCurrentPreferences) {
        this.workerCurrentPreferences = workerCurrentPreferences;
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