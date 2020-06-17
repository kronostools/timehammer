package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.common.utils.CommonUtils;
import io.vertx.mutiny.core.buffer.Buffer;

public class ComunytekStatusForm {
    private static final String ACTION_HOURS_REPORTED = "LISTRH";

    private final String sessionId;
    private final String par1;
    private final String par2;
    private final String par3;
    private final String par4;

    private ComunytekStatusForm(final String sessionId, final String username, final String day) {
        this.sessionId = sessionId;
        this.par1 = ACTION_HOURS_REPORTED;
        this.par2 = username;
        this.par3 = username;
        this.par4 = day;
    }

    public static class Builder {
        private String sessionId;
        private String username;
        private String day;

        private Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public Builder sessionId(final String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder username(final String username) {
            this.username = username;
            return this;
        }

        public Builder day(final String day) {
            this.day = day;
            return this;
        }

        public ComunytekStatusForm build() {
            return new ComunytekStatusForm(sessionId, username, day);
        }
    }

    public Buffer toBuffer() {
        return Buffer.buffer(CommonUtils.stringFormat("sessionId={}&par_1={}&par_2={}&par_3={}&par_4={}", sessionId, par1, par2, par3, par4));
    }
}