package com.kronostools.timehammer.comunytek.health;

import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;

@Readiness
@ApplicationScoped
public class ComunytekConnectionHealthCheck implements HealthCheck {
    private final ComunytekClient comunytekClient;

    private static final String COMUNYTEK_CONNECTION_HEALTH_CHECK = "Comunytek connection health check";

    public ComunytekConnectionHealthCheck(final ComunytekClient comunytekClient) {
        this.comunytekClient = comunytekClient;
    }

    @Override
    public HealthCheckResponse call() {
        return comunytekClient.about()
                .map(success -> success ? HealthCheckResponse.named(COMUNYTEK_CONNECTION_HEALTH_CHECK).up().build() : HealthCheckResponse.named(COMUNYTEK_CONNECTION_HEALTH_CHECK).down().build())
                .await().indefinitely();
    }
}