package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

@JsonDeserialize(builder = TelegramChatbotNotificationMessageBuilder.class)
public class TelegramChatbotNotificationMessage extends TelegramChatbotMessage {
    TelegramChatbotNotificationMessage(LocalDateTime generated, String chatId, Long messageId) {
        super(generated, chatId, messageId);
    }
}