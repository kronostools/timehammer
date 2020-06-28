package com.kronostools.timehammer.commandprocessor.config;

import com.kronostools.timehammer.common.config.ExpirationConfig;
import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "timehammer.chatbot.update-password-request-cache", namingStrategy= ConfigProperties.NamingStrategy.KEBAB_CASE)
public class ChatbotUpdatePasswordRequestCacheConfig {
    private ExpirationConfig expiration;

    public ExpirationConfig getExpiration() {
        return expiration;
    }

    public void setExpiration(ExpirationConfig expiration) {
        this.expiration = expiration;
    }
}