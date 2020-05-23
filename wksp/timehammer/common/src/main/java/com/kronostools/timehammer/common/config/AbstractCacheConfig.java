package com.kronostools.timehammer.common.config;

public abstract class AbstractCacheConfig {
    private ExpirationConfig expiration;

    public ExpirationConfig getExpiration() {
        return expiration;
    }

    public void setExpiration(ExpirationConfig expiration) {
        this.expiration = expiration;
    }
}