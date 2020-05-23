package com.kronostools.timehammer.comunytek.config;

import com.kronostools.timehammer.common.config.ExpirationConfig;
import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "timehammer.comunytek.login-cache", namingStrategy= ConfigProperties.NamingStrategy.KEBAB_CASE)
public class LoginCacheConfig {
    private ExpirationConfig expiration;

    public ExpirationConfig getExpiration() {
        return expiration;
    }

    public void setExpiration(ExpirationConfig expiration) {
        this.expiration = expiration;
    }
}