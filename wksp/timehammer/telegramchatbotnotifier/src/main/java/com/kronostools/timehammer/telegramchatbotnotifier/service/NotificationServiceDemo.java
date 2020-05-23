package com.kronostools.timehammer.telegramchatbotnotifier.service;

import org.apache.camel.CamelContext;

public class NotificationServiceDemo extends AbstractNotificationService {

    public NotificationServiceDemo(final CamelContext camelContext) {
        super(camelContext);
    }

    @Override
    protected boolean isDemoMode() {
        return true;
    }
}