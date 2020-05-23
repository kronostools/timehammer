package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class TelegramChatbotNotificationMessageBuilder extends TelegramChatbotMessageBuilder<TelegramChatbotNotificationMessageBuilder> {
    private String text;

    public static TelegramChatbotNotificationMessageBuilder copy(final TelegramChatbotInputMessage telegramChatbotInputMessage) {
        return Optional.ofNullable(telegramChatbotInputMessage)
                .map(tcim -> new TelegramChatbotNotificationMessageBuilder()
                        .generated(tcim.getGenerated())
                        .chatId(tcim.getChatId())
                        .messageId(tcim.getMessageId()))
                .orElse(null);
    }

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
                        .messageId(tcnm.getMessageId())
                        .text(tcnm.getText()))
                .orElse(null);
    }

    public TelegramChatbotNotificationMessageBuilder text(final String text) {
        this.text = text;
        return this;
    }

    public TelegramChatbotNotificationMessage build() {
        return new TelegramChatbotNotificationMessage(generated, chatId, messageId, text);
    }
}