package com.kronostools.timehammer.telegramchatbotnotifier.service;

import org.apache.camel.CamelContext;

public class NotificationServiceReal extends AbstractNotificationService {

    public NotificationServiceReal(final CamelContext camelContext) {
        super(camelContext);
    }

    @Override
    protected boolean isDemoMode() {
        return false;
    }
}