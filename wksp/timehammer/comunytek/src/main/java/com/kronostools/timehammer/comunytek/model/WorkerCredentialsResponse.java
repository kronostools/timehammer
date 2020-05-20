package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.comunytek.constants.CredentialsResponse;

public class WorkerCredentialsResponse {
    private CredentialsResponse response;
    private String externalPassword;

    public boolean isSuccessful() {
        return response != null && response.isSuccessful();
    }

    public CredentialsResponse getResponse() {
        return response;
    }

    public void setResponse(CredentialsResponse response) {
        this.response = response;
    }

    public String getExternalPassword() {
        return externalPassword;
    }

    public void setExternalPassword(String externalPassword) {
        this.externalPassword = externalPassword;
    }
}
