package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class CredentialPhaseBuilder extends PhaseBuilder<CredentialPhaseBuilder> {
    private String externalPassword;

    public static CredentialPhase copyAndBuild(final CredentialPhase credentialPhase) {
        return Optional.ofNullable(credentialPhase)
                .map(cp -> CredentialPhaseBuilder.copy(cp).build())
                .orElse(null);
    }

    public static CredentialPhaseBuilder copy(final CredentialPhase credentialPhase) {
        return Optional.ofNullable(credentialPhase)
                .map(cp -> new CredentialPhaseBuilder()
                        .errorMessage(cp.getErrorMessage())
                        .externalPassword(cp.getExternalPassword()))
                .orElse(null);
    }

    public CredentialPhaseBuilder externalPassword(final String externalPassword) {
        this.externalPassword = externalPassword;
        return this;
    }

    public CredentialPhase build() {
        final CredentialPhase result = new CredentialPhase(errorMessage);
        result.setExternalPassword(externalPassword);

        return result;
    }
}
