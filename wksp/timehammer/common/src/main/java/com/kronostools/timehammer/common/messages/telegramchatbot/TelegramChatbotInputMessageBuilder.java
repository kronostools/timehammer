package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.ChatbotCommand;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class TelegramChatbotInputMessageBuilder extends TelegramChatbotMessageBuilder<TelegramChatbotInputMessageBuilder> {
    private ChatbotCommand command;
    private String rawCommand;
    private String text;
    private WorkerCurrentPreferencesPhase workerCurrentPreferencesPhase;

    public static TelegramChatbotInputMessage copyAndBuild(final TelegramChatbotInputMessage chatbotMessage) {
        return Optional.ofNullable(chatbotMessage)
                .map(ccm -> TelegramChatbotInputMessageBuilder.copy(ccm).build())
                .orElse(null);
    }

    public static TelegramChatbotInputMessageBuilder copy(final TelegramChatbotInputMessage chatbotMessage) {
        return Optional.ofNullable(chatbotMessage)
                .map(ccm -> new TelegramChatbotInputMessageBuilder()
                        .generated(ccm.getGenerated())
                        .chatId(ccm.getChatId())
                        .messageId(ccm.getMessageId())
                        .command(ccm.getCommand())
                        .rawCommand(ccm.getRawCommand())
                        .text(ccm.getText())
                        .workerCurrentPreferencesPhase(ccm.getWorkerCurrentPreferencesPhase()))
                .orElse(null);
    }

    public TelegramChatbotInputMessageBuilder command(final ChatbotCommand command) {
        this.command = command;
        return this;
    }

    public TelegramChatbotInputMessageBuilder rawCommand(final String rawCommand) {
        this.rawCommand = rawCommand;
        return this;
    }

    public TelegramChatbotInputMessageBuilder text(final String text) {
        this.text = text;
        return this;
    }

    public TelegramChatbotInputMessageBuilder workerCurrentPreferencesPhase(final WorkerCurrentPreferencesPhase workerCurrentPreferencesPhase) {
        this.workerCurrentPreferencesPhase = workerCurrentPreferencesPhase;
        return this;
    }

    public TelegramChatbotInputMessage build() {
        final TelegramChatbotInputMessage result = new TelegramChatbotInputMessage(generated, chatId, messageId, text);
        result.setCommand(command);
        result.setRawCommand(rawCommand);
        result.setWorkerCurrentPreferencesPhase(workerCurrentPreferencesPhase);

        return result;
    }
}