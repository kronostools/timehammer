package com.kronostools.timehammer.commandprocessor.config;

import com.kronostools.timehammer.common.config.AbstractCacheConfig;
import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "timehammer.chatbot.registration-request-cache", namingStrategy= ConfigProperties.NamingStrategy.KEBAB_CASE)
public class ChatbotRegistrationRequestCacheConfig extends AbstractCacheConfig {
}