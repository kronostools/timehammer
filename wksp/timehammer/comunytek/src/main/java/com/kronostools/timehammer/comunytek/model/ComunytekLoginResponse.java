package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.comunytek.constants.ComunytekLoginResult;

public class ComunytekLoginResponse extends ComunytekResponse<ComunytekLoginResult> {
    private String sessionId;
    private String username;
    private String fullname;

    ComunytekLoginResponse(final ComunytekLoginResult result, final String errorMessage) {
        super(result, errorMessage);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}