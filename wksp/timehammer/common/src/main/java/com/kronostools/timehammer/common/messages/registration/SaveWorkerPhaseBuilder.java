package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.PhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class SaveWorkerPhaseBuilder extends PhaseBuilder<SimpleResult, SaveWorkerPhaseBuilder> {
    public static SaveWorkerPhase copyAndBuild(final SaveWorkerPhase saveWorkerPhase) {
        return Optional.ofNullable(saveWorkerPhase)
                .map(swp -> SaveWorkerPhaseBuilder.copy(swp).build())
                .orElse(null);
    }

    public static SaveWorkerPhaseBuilder copy(final SaveWorkerPhase saveWorkerPhase) {
        return Optional.ofNullable(saveWorkerPhase)
                .map(swp -> new SaveWorkerPhaseBuilder()
                        .result(swp.getResult())
                        .errorMessage(swp.getErrorMessage()))
                .orElse(null);
    }

    public SaveWorkerPhase build() {
        return new SaveWorkerPhase(result, errorMessage);
    }
}