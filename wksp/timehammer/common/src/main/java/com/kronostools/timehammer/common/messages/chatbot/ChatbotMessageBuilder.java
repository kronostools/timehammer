package com.kronostools.timehammer.common.messages.chatbot;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.ChatbotCommand;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class ChatbotMessageBuilder extends ChatbotInputMessageBuilder<ChatbotMessageBuilder> {
    private ChatbotCommand command;
    private String rawCommand;
    private String text;

    public static ChatbotMessage copyAndBuild(final ChatbotMessage chatbotMessage) {
        return Optional.ofNullable(chatbotMessage)
                .map(ccm -> ChatbotMessageBuilder.copy(ccm).build())
                .orElse(null);
    }

    public static ChatbotMessageBuilder copy(final ChatbotMessage chatbotMessage) {
        return Optional.ofNullable(chatbotMessage)
                .map(ccm -> new ChatbotMessageBuilder()
                        .generated(ccm.getGenerated())
                        .chatId(ccm.getChatId())
                        .messageId(ccm.getMessageId())
                        .command(ccm.getCommand())
                        .rawCommand(ccm.getRawCommand()))
                .orElse(null);
    }

    public ChatbotMessageBuilder command(final ChatbotCommand command) {
        this.command = command;
        return this;
    }

    public ChatbotMessageBuilder rawCommand(final String rawCommand) {
        this.rawCommand = rawCommand;
        return this;
    }

    public ChatbotMessageBuilder text(final String text) {
        this.text = text;
        return this;
    }

    public ChatbotMessage build() {
        final ChatbotMessage result = new ChatbotMessage(generated, chatId, messageId, text);
        result.setCommand(command);
        result.setRawCommand(rawCommand);

        return result;
    }
}