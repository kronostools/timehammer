package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.PhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class SaveWorkerStatusPhaseBuilder extends PhaseBuilder<SimpleResult, SaveWorkerStatusPhaseBuilder> {

    public static SaveWorkerStatusPhase copyAndBuild(final SaveWorkerStatusPhase saveWorkerStatusPhase) {
        return Optional.ofNullable(saveWorkerStatusPhase)
                .map(swsp -> SaveWorkerStatusPhaseBuilder.copy(swsp).build())
                .orElse(null);
    }

    public static SaveWorkerStatusPhaseBuilder copy(final SaveWorkerStatusPhase saveWorkerStatusPhase) {
        return Optional.ofNullable(saveWorkerStatusPhase)
                .map(swsp -> new SaveWorkerStatusPhaseBuilder()
                        .result(swsp.getResult())
                        .errorMessage(swsp.getErrorMessage()))
                .orElse(null);
    }

    public SaveWorkerStatusPhase build() {
        return new SaveWorkerStatusPhase(result, errorMessage);
    }
}