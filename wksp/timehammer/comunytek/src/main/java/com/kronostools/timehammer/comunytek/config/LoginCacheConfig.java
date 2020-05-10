package com.kronostools.timehammer.comunytek.config;

import io.quarkus.arc.config.ConfigProperties;

import java.util.concurrent.TimeUnit;

@ConfigProperties(prefix = "timehammer.comunytek.login-cache", namingStrategy= ConfigProperties.NamingStrategy.KEBAB_CASE)
public class LoginCacheConfig {
    private ExpirationConfig expiration;

    public static class ExpirationConfig {
        private Integer qty;
        private TimeUnit unit;

        public Integer getQty() {
            return qty;
        }

        public void setQty(Integer qty) {
            this.qty = qty;
        }

        public TimeUnit getUnit() {
            return unit;
        }

        public void setUnit(TimeUnit unit) {
            this.unit = unit;
        }
    }

    public ExpirationConfig getExpiration() {
        return expiration;
    }

    public void setExpiration(ExpirationConfig expiration) {
        this.expiration = expiration;
    }
}