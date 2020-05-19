package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class TelegramChatbotNotificationMessageBuilder extends TelegramChatbotMessageBuilder<TelegramChatbotNotificationMessageBuilder> {

    public static TelegramChatbotNotificationMessage copyAndBuild(final TelegramChatbotNotificationMessage telegramChatbotNotificationMessage) {
        return Optional.ofNullable(telegramChatbotNotificationMessage)
                .map(tcnm -> TelegramChatbotNotificationMessageBuilder.copy(tcnm).build())
                .orElse(null);
    }

    public static TelegramChatbotNotificationMessageBuilder copy(final TelegramChatbotNotificationMessage telegramChatbotNotificationMessage) {
        return Optional.ofNullable(telegramChatbotNotificationMessage)
                .map(tcnm -> new TelegramChatbotNotificationMessageBuilder()
                        .generated(tcnm.getGenerated())
                        .chatId(tcnm.getChatId())
                        .messageId(tcnm.getMessageId()))
                .orElse(null);
    }

    public TelegramChatbotNotificationMessage build() {
        return new TelegramChatbotNotificationMessage(generated, chatId, messageId);
    }
}