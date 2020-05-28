package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.comunytek.constants.ComunytekLoginResult;

public class WorkerCredentialsResponse {
    private ComunytekLoginResult response;
    private String externalPassword;

    public boolean isSuccessful() {
        return response != null && response.isSuccessful();
    }

    public ComunytekLoginResult getResponse() {
        return response;
    }

    public void setResponse(ComunytekLoginResult response) {
        this.response = response;
    }

    public String getExternalPassword() {
        return externalPassword;
    }

    public void setExternalPassword(String externalPassword) {
        this.externalPassword = externalPassword;
    }
}
