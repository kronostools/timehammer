package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.kronostools.timehammer.common.messages.PlatformMessageBuilder;

public abstract class TelegramChatbotMessageBuilder<B> extends PlatformMessageBuilder<B> {
    protected String chatId;
    protected Long messageId;

    public B chatId(final String chatId) {
        this.chatId = chatId;
        return (B) this;
    }

    public B messageId(final Long messageId) {
        this.messageId = messageId;
        return (B) this;
    }
}