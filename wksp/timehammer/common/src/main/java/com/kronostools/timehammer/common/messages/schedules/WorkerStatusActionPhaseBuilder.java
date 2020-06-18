package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.WorkerStatusAction;
import com.kronostools.timehammer.common.messages.PhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class WorkerStatusActionPhaseBuilder extends PhaseBuilder<SimpleResult, WorkerStatusActionPhaseBuilder> {
    private WorkerStatusAction workerStatusAction;

    public static WorkerStatusActionPhase copyAndBuild(final WorkerStatusActionPhase workerStatusActionPhase) {
        return Optional.ofNullable(workerStatusActionPhase)
                .map(wsap -> WorkerStatusActionPhaseBuilder.copy(wsap).build())
                .orElse(null);
    }

    public static WorkerStatusActionPhaseBuilder copy(final WorkerStatusActionPhase workerStatusActionPhase) {
        return Optional.ofNullable(workerStatusActionPhase)
                .map(wsap -> new WorkerStatusActionPhaseBuilder()
                        .result(wsap.getResult())
                        .errorMessage(wsap.getErrorMessage())
                        .workerStatusAction(wsap.getWorkerStatusAction()))
                .orElse(null);
    }

    public WorkerStatusActionPhaseBuilder workerStatusAction(final WorkerStatusAction workerStatusAction) {
        this.workerStatusAction = workerStatusAction;
        return this;
    }

    public WorkerStatusActionPhase build() {
        WorkerStatusActionPhase wsap = new WorkerStatusActionPhase(result, errorMessage);
        wsap.setWorkerStatusAction(workerStatusAction);

        return wsap;
    }
}