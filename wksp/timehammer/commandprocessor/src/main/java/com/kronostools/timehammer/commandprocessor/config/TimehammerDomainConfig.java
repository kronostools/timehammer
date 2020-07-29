package com.kronostools.timehammer.commandprocessor.config;

import io.quarkus.arc.config.ConfigProperties;
import io.quarkus.arc.config.ConfigProperties.NamingStrategy;

@ConfigProperties(prefix = "timehammer.domain", namingStrategy= NamingStrategy.KEBAB_CASE)
public class TimehammerDomainConfig {
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getRegisterUrl(final String internalId) {
        return baseUrl + "/auth/register.html?registrationRequestId=" + internalId;
    }

    public String getUnregisterUrl(final String internalId) {
        return baseUrl + "/auth/unregister.html?internalId=" + internalId;
    }

    public String getSettingsUrl(final String internalId) {
        return baseUrl + "/auth/settings.html?internalId=" + internalId;
    }

    public String getUpdatePasswordUrl(final String internalId) {
        return baseUrl + "/auth/updatePassword.html?internalId=" + internalId;
    }

    public String getHelpUrl() {
        return baseUrl + "/faq.html";
    }
}