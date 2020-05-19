package com.kronostools.timehammer.telegramchatbotnotifier.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessage;
import com.kronostools.timehammer.telegramchatbotnotifier.service.NotificationService;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class NotificationSender {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationSender.class);

    private final NotificationService notificationService;

    public NotificationSender(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Incoming(Channels.NOTIFICATION_TELEGRAM)
    public CompletionStage<Void> process(final Message<TelegramChatbotNotificationMessage> message) {
        LOG.info("Processing new notification ...");

        return notificationService.notify(message);
    }
}