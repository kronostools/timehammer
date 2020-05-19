package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class TelegramChatbotAnswerMessageBuilder extends TelegramChatbotInputMessageBuilder<TelegramChatbotAnswerMessageBuilder> {
    private String answer;

    public static TelegramChatbotAnswerMessage copyAndBuild(final TelegramChatbotAnswerMessage telegramChatbotAnswerMessage) {
        return Optional.ofNullable(telegramChatbotAnswerMessage)
                .map(cam -> TelegramChatbotAnswerMessageBuilder.copy(cam).build())
                .orElse(null);
    }

    public static TelegramChatbotAnswerMessageBuilder copy(final TelegramChatbotAnswerMessage telegramChatbotAnswerMessage) {
        return Optional.ofNullable(telegramChatbotAnswerMessage)
                .map(cam -> new TelegramChatbotAnswerMessageBuilder()
                        .generated(cam.getGenerated())
                        .chatId(cam.getChatId())
                        .messageId(cam.getMessageId())
                        .answer(cam.getAnswer()))
                .orElse(null);
    }

    public TelegramChatbotAnswerMessageBuilder answer(final String answer) {
        this.answer = answer;
        return this;
    }

    public TelegramChatbotAnswerMessage build() {
        final TelegramChatbotAnswerMessage result = new TelegramChatbotAnswerMessage(generated, chatId, messageId);
        result.setAnswer(answer);
        
        return result;
    }
}