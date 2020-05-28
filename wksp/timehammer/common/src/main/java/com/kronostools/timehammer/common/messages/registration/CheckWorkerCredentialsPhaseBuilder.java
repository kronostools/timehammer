package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.PhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.WorkerCredentialsResult;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class CheckWorkerCredentialsPhaseBuilder extends PhaseBuilder<WorkerCredentialsResult, CheckWorkerCredentialsPhaseBuilder> {

    public static CheckWorkerCredentialsPhase copyAndBuild(final CheckWorkerCredentialsPhase checkWorkerCredentialsPhase) {
        return Optional.ofNullable(checkWorkerCredentialsPhase)
                .map(cwcp -> CheckWorkerCredentialsPhaseBuilder.copy(cwcp).build())
                .orElse(null);
    }

    public static CheckWorkerCredentialsPhaseBuilder copy(final CheckWorkerCredentialsPhase checkWorkerCredentialsPhase) {
        return Optional.ofNullable(checkWorkerCredentialsPhase)
                .map(cwcp -> new CheckWorkerCredentialsPhaseBuilder()
                        .result(cwcp.getResult())
                        .errorMessage(cwcp.getErrorMessage()))
                .orElse(null);
    }

    public CheckWorkerCredentialsPhase build() {
        return new CheckWorkerCredentialsPhase(result, errorMessage);
    }
}