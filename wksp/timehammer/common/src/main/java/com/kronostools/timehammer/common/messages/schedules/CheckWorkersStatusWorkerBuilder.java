package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.schedules.model.WorkerCurrentPreferences;
import com.kronostools.timehammer.common.messages.schedules.model.WorkerCurrentPreferencesBuilder;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class CheckWorkersStatusWorkerBuilder extends ProcessableBatchScheduleMessageBuilder<CheckWorkersStatusWorkerBuilder> {
    private WorkerCurrentPreferences workerCurrentPreferences;
    private GetWorkerStatusPhase workerStatusPhase;
    private WorkerStatusActionPhase workerStatusActionPhase;

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
                    .workerCurrentPreferences(WorkerCurrentPreferencesBuilder.copyAndBuild(w.getWorkerCurrentPreferences()))
                    .workerStatusPhase(GetWorkerStatusPhaseBuilder.copyAndBuild(w.getWorkerStatusPhase()))
                    .workerStatusActionPhase(WorkerStatusActionPhaseBuilder.copyAndBuild(w.getWorkerStatusActionPhase())))
                .orElse(null);
    }

    public CheckWorkersStatusWorkerBuilder workerCurrentPreferences(final WorkerCurrentPreferences workerCurrentPreferences) {
        this.workerCurrentPreferences = workerCurrentPreferences;
        return this;
    }

    public CheckWorkersStatusWorkerBuilder workerStatusPhase(final GetWorkerStatusPhase workerStatusPhase) {
        this.workerStatusPhase = workerStatusPhase;
        return this;
    }

    public CheckWorkersStatusWorkerBuilder workerStatusActionPhase(final WorkerStatusActionPhase workerStatusActionPhase) {
        this.workerStatusActionPhase = workerStatusActionPhase;
        return this;
    }

    public CheckWorkersStatusWorker build() {
        final CheckWorkersStatusWorker result = new CheckWorkersStatusWorker(generated, name, executionId, batchSize);
        result.setWorkerCurrentPreferences(workerCurrentPreferences);
        result.setWorkerStatusPhase(workerStatusPhase);
        result.setWorkerStatusActionPhase(workerStatusActionPhase);

        return result;
    }
}