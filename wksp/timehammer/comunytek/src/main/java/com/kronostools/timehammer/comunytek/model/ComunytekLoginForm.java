package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.common.utils.CommonUtils;
import io.vertx.mutiny.core.buffer.Buffer;

public class ComunytekLoginForm {
    public static final String FAKE_SESSIONID = "11111111";
    private static final String S = "S";

    private static final String ACTION_LOGIN = "LOGIN";

    private final String sessionId;
    private final String par1;
    private final String par2;
    private final String par3;
    private final String par4;

    private ComunytekLoginForm(final String username, final String password) {
        this.sessionId = FAKE_SESSIONID;
        this.par1 = ACTION_LOGIN;
        this.par2 = username;
        this.par3 = password;
        this.par4 = S;
    }

    public static class Builder {
        private String username;
        private String password;

        private Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public Builder username(final String username) {
            this.username = username;
            return this;
        }

        public Builder password(final String password) {
            this.password = password;
            return this;
        }

        public ComunytekLoginForm build() {
            return new ComunytekLoginForm(username, password);
        }
    }

    public Buffer toBuffer() {
        return Buffer.buffer(CommonUtils.stringFormat("sessionId={}&par_1={}&par_2={}&par_3={}&par_4={}", sessionId, par1, par2, par3, par4));
    }
}