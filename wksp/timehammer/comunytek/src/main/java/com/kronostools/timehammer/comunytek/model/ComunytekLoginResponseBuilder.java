package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.comunytek.constants.ComunytekLoginResult;

public class ComunytekLoginResponseBuilder extends ComunytekResponseBuilder<ComunytekLoginResult, ComunytekLoginResponseBuilder> {
    private String sessionId;
    private String username;
    private String fullname;

    public ComunytekLoginResponseBuilder sessionId(final String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public ComunytekLoginResponseBuilder username(final String username) {
        this.username = username;
        return this;
    }

    public ComunytekLoginResponseBuilder fullname(final String fullname) {
        this.fullname = fullname;
        return this;
    }

    public ComunytekLoginResponse build() {
        final ComunytekLoginResponse clr = new ComunytekLoginResponse(result, errorMessage);
        clr.setSessionId(sessionId);
        clr.setUsername(username);
        clr.setFullname(fullname);

        return clr;
    }
}