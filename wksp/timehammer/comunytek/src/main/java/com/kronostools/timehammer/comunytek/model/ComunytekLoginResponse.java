package com.kronostools.timehammer.comunytek.model;

public class ComunytekLoginResponse {
    private final boolean successful;
    private final String sessionId;
    private final String username;
    private final String fullname;

    private ComunytekLoginResponse(final boolean successful) {
        this.successful = successful;
        this.sessionId = null;
        this.username = null;
        this.fullname = null;
    }

    private ComunytekLoginResponse(final boolean successful, final String sessionId, final String username, final String fullname) {
        this.successful = successful;
        this.sessionId = sessionId;
        this.username = username;
        this.fullname = fullname;
    }

    public static class Builder {
        private String sessionId;
        private String username;
        private String fullname;

        private Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public static ComunytekLoginResponse buildUnsuccessful() {
            return new ComunytekLoginResponse(false);
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

        public ComunytekLoginResponse build() {
            return new ComunytekLoginResponse(true, sessionId, username, fullname);
        }
    }

    public boolean isSuccessful() {
        return successful;
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