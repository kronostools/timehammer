package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.PhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.WorkerCredentialsResult;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class CheckWorkerCredentialsPhaseBuilder extends PhaseBuilder<WorkerCredentialsResult, CheckWorkerCredentialsPhaseBuilder> {
    private String fullname;

    public static CheckWorkerCredentialsPhase copyAndBuild(final CheckWorkerCredentialsPhase checkWorkerCredentialsPhase) {
        return Optional.ofNullable(checkWorkerCredentialsPhase)
                .map(cwcp -> CheckWorkerCredentialsPhaseBuilder.copy(cwcp).build())
                .orElse(null);
    }

    public static CheckWorkerCredentialsPhaseBuilder copy(final CheckWorkerCredentialsPhase checkWorkerCredentialsPhase) {
        return Optional.ofNullable(checkWorkerCredentialsPhase)
                .map(cwcp -> new CheckWorkerCredentialsPhaseBuilder()
                        .result(cwcp.getResult())
                        .errorMessage(cwcp.getErrorMessage())
                        .fullname(cwcp.getFullname()))
                .orElse(null);
    }

    public CheckWorkerCredentialsPhaseBuilder fullname(final String fullname) {
        this.fullname = fullname;
        return this;
    }

    public CheckWorkerCredentialsPhase build() {
        final CheckWorkerCredentialsPhase cwcp = new CheckWorkerCredentialsPhase(result, errorMessage);
        cwcp.setFullname(fullname);

        return cwcp;
    }
}