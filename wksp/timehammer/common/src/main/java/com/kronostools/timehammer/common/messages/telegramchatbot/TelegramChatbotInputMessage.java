package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.kronostools.timehammer.common.messages.PlatformMessage;

import java.time.LocalDateTime;

public abstract class TelegramChatbotInputMessage extends PlatformMessage {
    private String chatId;
    private Long messageId;

    TelegramChatbotInputMessage(final LocalDateTime generated, final String chatId, final Long messageId) {
        super(generated);
        this.chatId = chatId;
        this.messageId = messageId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
}