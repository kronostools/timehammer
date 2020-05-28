package com.kronostools.timehammer.comunytek.model;

import java.time.LocalDateTime;

public class CachedWorkerCredentials {
    private String externalPassword;
    private boolean invalid;
    private LocalDateTime invalidSince;

    public String getExternalPassword() {
        return externalPassword;
    }

    public void setExternalPassword(String externalPassword) {
        this.externalPassword = externalPassword;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public LocalDateTime getInvalidSince() {
        return invalidSince;
    }

    public void setInvalidSince(LocalDateTime invalidSince) {
        this.invalidSince = invalidSince;
    }
}