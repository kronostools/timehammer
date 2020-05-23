package com.kronostools.timehammer.telegramchatbotnotifier.config;

import com.kronostools.timehammer.telegramchatbotnotifier.service.NotificationService;
import com.kronostools.timehammer.telegramchatbotnotifier.service.NotificationServiceDemo;
import com.kronostools.timehammer.telegramchatbotnotifier.service.NotificationServiceReal;
import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.profile.UnlessBuildProfile;
import org.apache.camel.CamelContext;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class NotificationServiceConfig {
    @Produces
    @ApplicationScoped
    @DefaultBean
    public NotificationService realNotificationService(final CamelContext camelContext) {
        return new NotificationServiceReal(camelContext);
    }

    @Produces
    @ApplicationScoped
    @UnlessBuildProfile("prod")
    public NotificationService demoNotificationService(final CamelContext camelContext) {
        return new NotificationServiceDemo(camelContext);
    }
}