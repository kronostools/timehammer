package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = CredentialPhaseBuilder.class)
public class CredentialPhase extends Phase {
    private String externalPassword;

    CredentialPhase(final String errorMessage) {
        super(errorMessage);
    }

    public String getExternalPassword() {
        return externalPassword;
    }

    public void setExternalPassword(String externalPassword) {
        this.externalPassword = externalPassword;
    }
}