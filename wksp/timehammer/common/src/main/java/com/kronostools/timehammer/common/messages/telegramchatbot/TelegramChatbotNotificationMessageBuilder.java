package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.telegramchatbot.model.KeyboardOption;
import com.kronostools.timehammer.common.messages.telegramchatbot.model.KeyboardOptionBuilder;

import java.util.List;
import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class TelegramChatbotNotificationMessageBuilder extends TelegramChatbotMessageBuilder<TelegramChatbotNotificationMessageBuilder> {
    private String text;
    private Boolean clearPreviousKeyboard;
    private List<KeyboardOption> keyboard;

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
                        .text(tcnm.getText())
                        .keyboard(KeyboardOptionBuilder.copyAndBuild(tcnm.getKeyboard())))
                .orElse(null);
    }

    public TelegramChatbotNotificationMessageBuilder text(final String text) {
        this.text = text;
        return this;
    }

    public TelegramChatbotNotificationMessageBuilder clearPreviousKeyboard(final boolean clearPreviousKeyboard) {
        this.clearPreviousKeyboard = clearPreviousKeyboard;
        return this;
    }

    public TelegramChatbotNotificationMessageBuilder keyboard(final List<KeyboardOption> keyboard) {
        this.clearPreviousKeyboard = true;
        this.keyboard = keyboard;
        return this;
    }

    public TelegramChatbotNotificationMessage build() {
        final TelegramChatbotNotificationMessage tcnm = new TelegramChatbotNotificationMessage(generated, chatId, messageId, text);
        tcnm.setClearPreviousKeyboard(clearPreviousKeyboard != null ? clearPreviousKeyboard : false);
        tcnm.setKeyboard(keyboard);

        return tcnm;
    }
}