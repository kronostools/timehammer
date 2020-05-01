package com.kronostools.timehammer.chatbot.health;

import com.kronostools.timehammer.chatbot.restclient.TelegramRestClient;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Readiness
@ApplicationScoped
public class TelegramChatbotHealthCheck implements HealthCheck {
    @Inject
    @RestClient
    TelegramRestClient telegramRestClient;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Telegram chatbot connection health check");

        try {
            telegramRestClient.getMe();
            responseBuilder.up();
        } catch (Exception e) {
            responseBuilder.down();
        }

        return responseBuilder.build();
    }
}
