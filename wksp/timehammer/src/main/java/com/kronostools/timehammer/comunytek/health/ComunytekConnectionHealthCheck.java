package com.kronostools.timehammer.comunytek.health;

import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;

@Readiness
@ApplicationScoped
public class ComunytekConnectionHealthCheck implements HealthCheck {
    private final ComunytekClient comunytekClient;

    public ComunytekConnectionHealthCheck(final ComunytekClient comunytekClient) {
        this.comunytekClient = comunytekClient;
    }

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Comunytek connection health check");

        try {
            comunytekClient.about();
            responseBuilder.up();
        } catch (Exception e) {
            responseBuilder.down();
        }

        return responseBuilder.build();
    }
}