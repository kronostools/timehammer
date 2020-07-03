package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.PhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.WorkerStatusResult;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class UpdateWorkerStatusPhaseBuilder extends PhaseBuilder<WorkerStatusResult, UpdateWorkerStatusPhaseBuilder> {
    public static UpdateWorkerStatusPhase copyAndBuild(final UpdateWorkerStatusPhase updateWorkerStatusPhase) {
        return Optional.ofNullable(updateWorkerStatusPhase)
                .map(uwsp -> UpdateWorkerStatusPhaseBuilder.copy(uwsp).build())
                .orElse(null);
    }

    public static UpdateWorkerStatusPhaseBuilder copy(final UpdateWorkerStatusPhase updateWorkerStatusPhase) {
        return Optional.ofNullable(updateWorkerStatusPhase)
                .map(uwsp -> new UpdateWorkerStatusPhaseBuilder()
                        .result(uwsp.getResult())
                        .errorMessage(uwsp.getErrorMessage()))
                .orElse(null);
    }

    public UpdateWorkerStatusPhase build() {
        return new UpdateWorkerStatusPhase(result, errorMessage);
    }
}