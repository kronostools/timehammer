package com.kronostools.timehammer.comunytek.model;

public class ComunytekLoginResponse extends ComunytekResponse {
    private final String sessionId;
    private final String username;
    private final String fullname;

    ComunytekLoginResponse(final String errorMessage) {
        super(false, errorMessage);
        this.sessionId = null;
        this.username = null;
        this.fullname = null;
    }

    ComunytekLoginResponse(final String sessionId, final String username, final String fullname) {
        super(true, null);
        this.sessionId = sessionId;
        this.username = username;
        this.fullname = fullname;
    }

    public static class Builder implements ComunytekResponseBuilder<ComunytekLoginResponse> {
        private String sessionId;
        private String username;
        private String fullname;

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

        public Builder fullname(final String fullname) {
            this.fullname = fullname;
            return this;
        }

        @Override
        public ComunytekLoginResponse build() {
            return new ComunytekLoginResponse(sessionId, username, fullname);
        }

        @Override
        public ComunytekLoginResponse buildUnsuccessful(final String errorMessage) {
            return new ComunytekLoginResponse(errorMessage);
        }
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }
}