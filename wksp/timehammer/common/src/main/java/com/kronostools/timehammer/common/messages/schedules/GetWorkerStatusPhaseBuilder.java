package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.WorkerStatusAction;
import com.kronostools.timehammer.common.constants.WorkerStatusContext;
import com.kronostools.timehammer.common.messages.PhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class GetWorkerStatusPhaseBuilder extends PhaseBuilder<SimpleResult, GetWorkerStatusPhaseBuilder> {
    private WorkerStatusContext statusContext;
    private WorkerStatusAction statusAction;

    public static GetWorkerStatusPhase copyAndBuild(final GetWorkerStatusPhase getWorkerStatusPhase) {
        return Optional.ofNullable(getWorkerStatusPhase)
                .map(gwsp -> GetWorkerStatusPhaseBuilder.copy(gwsp).build())
                .orElse(null);
    }

    public static GetWorkerStatusPhaseBuilder copy(final GetWorkerStatusPhase getWorkerStatusPhase) {
        return Optional.ofNullable(getWorkerStatusPhase)
                .map(gwsp -> new GetWorkerStatusPhaseBuilder()
                        .result(gwsp.getResult())
                        .errorMessage(gwsp.getErrorMessage())
                        .statusContext(gwsp.getStatusContext())
                        .statusAction(gwsp.getStatusAction()))
                .orElse(null);
    }

    public GetWorkerStatusPhaseBuilder statusContext(final WorkerStatusContext statusContext) {
        this.statusContext = statusContext;
        return this;
    }

    public GetWorkerStatusPhaseBuilder statusAction(final WorkerStatusAction statusAction) {
        this.statusAction = statusAction;
        return this;
    }

    public GetWorkerStatusPhase build() {
        final GetWorkerStatusPhase gwsp = new GetWorkerStatusPhase(result, errorMessage);
        gwsp.setStatusContext(statusContext);
        gwsp.setStatusAction(statusAction);

        return gwsp;
    }
}