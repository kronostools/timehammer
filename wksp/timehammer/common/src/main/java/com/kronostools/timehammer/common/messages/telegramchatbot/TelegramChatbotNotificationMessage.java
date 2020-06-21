package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.telegramchatbot.model.KeyboardOption;

import java.time.LocalDateTime;
import java.util.List;

@JsonDeserialize(builder = TelegramChatbotNotificationMessageBuilder.class)
public class TelegramChatbotNotificationMessage extends TelegramChatbotMessage {
    private String text;
    private List<KeyboardOption> keyboard;

    TelegramChatbotNotificationMessage(final LocalDateTime generated, final String chatId, final Long messageId, final String text) {
        super(generated, chatId, messageId);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<KeyboardOption> getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(List<KeyboardOption> keyboard) {
        this.keyboard = keyboard;
    }
}