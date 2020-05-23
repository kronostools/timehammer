package com.kronostools.timehammer.telegramchatbotnotifier.service;

import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessage;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;

public interface NotificationService {
    CompletionStage<Void> notify(final Message<TelegramChatbotNotificationMessage> message);
}