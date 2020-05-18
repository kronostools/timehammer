package com.kronostools.timehammer.chatbot.config;

import io.quarkus.arc.config.ConfigProperties;
import io.quarkus.arc.config.ConfigProperties.NamingStrategy;

@ConfigProperties(prefix = "timehammer.chatbot", namingStrategy= NamingStrategy.KEBAB_CASE)
public class ChatbotConfig {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}