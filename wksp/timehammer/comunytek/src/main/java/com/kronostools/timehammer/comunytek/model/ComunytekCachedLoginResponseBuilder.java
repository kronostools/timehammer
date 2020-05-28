package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.comunytek.constants.ComunytekCachedLoginResult;
import com.kronostools.timehammer.comunytek.constants.ComunytekLoginResult;

import java.util.Optional;

public class ComunytekCachedLoginResponseBuilder extends ComunytekResponseBuilder<ComunytekCachedLoginResult, ComunytekCachedLoginResponseBuilder> {
    private String sessionId;
    private String username;
    private String fullname;

    public static ComunytekCachedLoginResponse copyAndBuild(final ComunytekLoginResponse comunytekLoginResponse) {
        return Optional.ofNullable(comunytekLoginResponse)
                .map(clr -> ComunytekCachedLoginResponseBuilder.copy(clr).build())
                .orElse(null);
    }

    public static ComunytekCachedLoginResponseBuilder copy(final ComunytekLoginResponse comunytekLoginResponse) {
        return Optional.ofNullable(comunytekLoginResponse)
                .map(clr -> {
                    final ComunytekCachedLoginResponseBuilder cclrb;

                    if (clr.isSuccessful()) {
                        cclrb = new ComunytekCachedLoginResponseBuilder()
                                .result(ComunytekCachedLoginResult.OK)
                                .username(clr.getUsername())
                                .sessionId(clr.getSessionId())
                                .fullname(clr.getFullname());
                    } else {
                        final ComunytekCachedLoginResult result;

                        if (clr.getResult() == ComunytekLoginResult.INVALID) {
                            result = ComunytekCachedLoginResult.INVALID;
                        } else {
                            result = ComunytekCachedLoginResult.UNEXPECTED_ERROR;
                        }

                        cclrb = new ComunytekCachedLoginResponseBuilder()
                                .result(result);
                    }

                    return cclrb;
                })
                .orElse(null);
    }

    public ComunytekCachedLoginResponseBuilder sessionId(final String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public ComunytekCachedLoginResponseBuilder username(final String username) {
        this.username = username;
        return this;
    }

    public ComunytekCachedLoginResponseBuilder fullname(final String fullname) {
        this.fullname = fullname;
        return this;
    }

    public ComunytekCachedLoginResponse build() {
        final ComunytekCachedLoginResponse clr = new ComunytekCachedLoginResponse(result, errorMessage);
        clr.setSessionId(sessionId);
        clr.setUsername(username);
        clr.setFullname(fullname);

        return clr;
    }
}