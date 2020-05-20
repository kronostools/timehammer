package com.kronostools.timehammer.comunytek.model;

import java.time.LocalDateTime;

public class CachedWorkerCredentials {
    private String externalPassword;
    private boolean expired;
    private LocalDateTime expiredSince;

    public String getExternalPassword() {
        return externalPassword;
    }

    public void setExternalPassword(String externalPassword) {
        this.externalPassword = externalPassword;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public LocalDateTime getExpiredSince() {
        return expiredSince;
    }

    public void setExpiredSince(LocalDateTime expiredSince) {
        this.expiredSince = expiredSince;
    }
}