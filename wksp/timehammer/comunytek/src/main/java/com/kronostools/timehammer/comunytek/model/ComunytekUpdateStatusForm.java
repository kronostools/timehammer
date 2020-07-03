package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.common.utils.CommonUtils;
import io.vertx.mutiny.core.buffer.Buffer;

public class ComunytekUpdateStatusForm {
    private static final String ACTION_REPORT_HOURS = "ADDRH";

    private final String sessionId;
    private final String par1;
    private final String par2;
    private final String par3;
    private final String par4;
    private final String par5;

    private ComunytekUpdateStatusForm(final String sessionId, final String username, final String action, final String comment) {
        this.sessionId = sessionId;
        this.par1 = ACTION_REPORT_HOURS;
        this.par2 = username;
        this.par3 = username;
        this.par4 = action;
        this.par5 = comment == null ? "" : comment.trim();
    }

    public static class Builder {
        private String sessionId;
        private String username;
        private String action;
        private String comment;

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

        public Builder action(final String action) {
            this.action = action;
            return this;
        }

        public Builder comment(final String comment) {
            this.comment = comment;
            return this;
        }

        public ComunytekUpdateStatusForm build() {
            return new ComunytekUpdateStatusForm(sessionId, username, action, comment);
        }
    }

    public Buffer toBuffer() {
        return Buffer.buffer(CommonUtils.stringFormat("sessionId={}&par_1={}&par_2={}&par_3={}&par_4={}&par_5={}", sessionId, par1, par2, par3, par4, par5));
    }
}