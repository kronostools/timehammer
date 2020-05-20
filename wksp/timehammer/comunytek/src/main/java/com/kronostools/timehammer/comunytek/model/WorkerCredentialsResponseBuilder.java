package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.comunytek.constants.CredentialsResponse;

public class WorkerCredentialsResponseBuilder {
    private CredentialsResponse response;
    private String externalPassword;

    public WorkerCredentialsResponseBuilder response(final CredentialsResponse response) {
        this.response = response;
        return this;
    }

    public WorkerCredentialsResponseBuilder externalPassword(final String credential) {
        this.externalPassword = credential;
        return this;
    }

    public WorkerCredentialsResponse build() {
        final WorkerCredentialsResponse result = new WorkerCredentialsResponse();
        result.setResponse(response);
        result.setExternalPassword(externalPassword);

        return result;
    }
}