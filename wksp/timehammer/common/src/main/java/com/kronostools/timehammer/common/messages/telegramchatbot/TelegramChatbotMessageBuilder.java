package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.ChatbotCommand;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class TelegramChatbotMessageBuilder extends TelegramChatbotInputMessageBuilder<TelegramChatbotMessageBuilder> {
    private ChatbotCommand command;
    private String rawCommand;
    private String text;

    public static TelegramChatbotMessage copyAndBuild(final TelegramChatbotMessage chatbotMessage) {
        return Optional.ofNullable(chatbotMessage)
                .map(ccm -> TelegramChatbotMessageBuilder.copy(ccm).build())
                .orElse(null);
    }

    public static TelegramChatbotMessageBuilder copy(final TelegramChatbotMessage chatbotMessage) {
        return Optional.ofNullable(chatbotMessage)
                .map(ccm -> new TelegramChatbotMessageBuilder()
                        .generated(ccm.getGenerated())
                        .chatId(ccm.getChatId())
                        .messageId(ccm.getMessageId())
                        .command(ccm.getCommand())
                        .rawCommand(ccm.getRawCommand()))
                .orElse(null);
    }

    public TelegramChatbotMessageBuilder command(final ChatbotCommand command) {
        this.command = command;
        return this;
    }

    public TelegramChatbotMessageBuilder rawCommand(final String rawCommand) {
        this.rawCommand = rawCommand;
        return this;
    }

    public TelegramChatbotMessageBuilder text(final String text) {
        this.text = text;
        return this;
    }

    public TelegramChatbotMessage build() {
        final TelegramChatbotMessage result = new TelegramChatbotMessage(generated, chatId, messageId, text);
        result.setCommand(command);
        result.setRawCommand(rawCommand);

        return result;
    }
}