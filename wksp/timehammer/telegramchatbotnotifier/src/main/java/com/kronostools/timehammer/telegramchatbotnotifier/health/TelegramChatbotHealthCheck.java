package com.kronostools.timehammer.telegramchatbotnotifier.health;

import com.kronostools.timehammer.telegramchatbotnotifier.client.TelegramClient;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;

@Readiness
@ApplicationScoped
public class TelegramChatbotHealthCheck implements HealthCheck {
    private final TelegramClient telegramClient;

    private static final String TELEGRAM_CONNECTION_HEALTH_CHECK = "Telegram chatbot connection health check";

    public TelegramChatbotHealthCheck(final TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }
    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Telegram chatbot connection health check");

        return telegramClient.getMe()
                .map(me -> me != null ? HealthCheckResponse.named(TELEGRAM_CONNECTION_HEALTH_CHECK).up().build() : HealthCheckResponse.named(TELEGRAM_CONNECTION_HEALTH_CHECK).down().build())
                .await().indefinitely();
    }
}