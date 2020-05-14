package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonPOJOBuilder(withPrefix = "")
public class CredentialPhaseBuilder extends PhaseBuilder<CredentialPhaseBuilder> {
    private String externalPassword;

    public static CredentialPhaseBuilder copy(final CredentialPhase credentialResult) {
        return new CredentialPhaseBuilder()
                .errorMessage(credentialResult.getErrorMessage())
                .externalPassword(credentialResult.getExternalPassword());
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
