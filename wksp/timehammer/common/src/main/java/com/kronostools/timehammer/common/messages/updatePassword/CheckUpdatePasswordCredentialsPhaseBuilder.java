package com.kronostools.timehammer.common.messages.updatePassword;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.PhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.WorkerCredentialsResult;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class CheckUpdatePasswordCredentialsPhaseBuilder extends PhaseBuilder<WorkerCredentialsResult, CheckUpdatePasswordCredentialsPhaseBuilder> {
    public static CheckUpdatePasswordCredentialsPhase copyAndBuild(final CheckUpdatePasswordCredentialsPhase checkUpdatePasswordCredentialsPhase) {
        return Optional.ofNullable(checkUpdatePasswordCredentialsPhase)
                .map(cupcp -> CheckUpdatePasswordCredentialsPhaseBuilder.copy(cupcp).build())
                .orElse(null);
    }

    public static CheckUpdatePasswordCredentialsPhaseBuilder copy(final CheckUpdatePasswordCredentialsPhase checkUpdatePasswordCredentialsPhase) {
        return Optional.ofNullable(checkUpdatePasswordCredentialsPhase)
                .map(cwcp -> new CheckUpdatePasswordCredentialsPhaseBuilder()
                        .result(cwcp.getResult())
                        .errorMessage(cwcp.getErrorMessage()))
                .orElse(null);
    }

    public CheckUpdatePasswordCredentialsPhase build() {
        final CheckUpdatePasswordCredentialsPhase cwcp = new CheckUpdatePasswordCredentialsPhase(result, errorMessage);

        return cwcp;
    }
}