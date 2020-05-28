package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.comunytek.constants.ComunytekLoginResult;

public class WorkerCredentialsResponseBuilder {
    private ComunytekLoginResult response;
    private String externalPassword;

    public WorkerCredentialsResponseBuilder response(final ComunytekLoginResult response) {
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