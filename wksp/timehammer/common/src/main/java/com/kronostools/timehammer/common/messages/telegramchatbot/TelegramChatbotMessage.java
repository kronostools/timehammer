package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.constants.ChatbotCommand;

import java.time.LocalDateTime;

@JsonDeserialize(builder = TelegramChatbotMessageBuilder.class)
public class TelegramChatbotMessage extends TelegramChatbotInputMessage {
    private ChatbotCommand command;
    private String rawCommand;
    private String text;

    TelegramChatbotMessage(final LocalDateTime generated, final String chatId, final Long messageId, final String text) {
        super(generated, chatId, messageId);
        this.text = text;
    }

    public ChatbotCommand getCommand() {
        return command;
    }

    public void setCommand(ChatbotCommand command) {
        this.command = command;
    }

    public String getRawCommand() {
        return rawCommand;
    }

    public void setRawCommand(String rawCommand) {
        this.rawCommand = rawCommand;
    }

    @JsonIgnore
    public boolean isCommandPresent() {
        return command != null || rawCommand != null;
    }

    @JsonIgnore
    public boolean isCommandUnknown() {
        return rawCommand != null;
    }
}