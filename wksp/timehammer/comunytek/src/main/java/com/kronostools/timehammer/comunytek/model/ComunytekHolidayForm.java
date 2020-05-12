package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.common.utils.CommonUtils;
import io.vertx.mutiny.core.buffer.Buffer;

public class ComunytekHolidayForm {
    private static final String N = "N";

    private static final String ACTION_HOLIDAYS = "LISTVAC";

    private final String sessionId;
    private final String par1;
    private final String par2;
    private final String par3;
    private final String par4;

    private ComunytekHolidayForm(final String sessionId, final String username) {
        this.sessionId = sessionId;
        this.par1 = ACTION_HOLIDAYS;
        this.par2 = username;
        this.par3 = username;
        this.par4 = N;
    }

    public static class Builder {
        private String sessionId;
        private String username;

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

        public ComunytekHolidayForm build() {
            return new ComunytekHolidayForm(sessionId, username);
        }
    }

    public Buffer toBuffer() {
        return Buffer.buffer(CommonUtils.stringFormat("sessionId={}&par_1={}&par_2={}&par_3={}&par_4={}", sessionId, par1, par2, par3, par4));
    }
}