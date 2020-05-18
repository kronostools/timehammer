package com.kronostools.timehammer.common.messages.chatbot;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class ChatbotAnswerMessageBuilder extends ChatbotInputMessageBuilder<ChatbotAnswerMessageBuilder> {
    private String answer;

    public static ChatbotAnswerMessage copyAndBuild(final ChatbotAnswerMessage chatbotAnswerMessage) {
        return Optional.ofNullable(chatbotAnswerMessage)
                .map(cam -> ChatbotAnswerMessageBuilder.copy(cam).build())
                .orElse(null);
    }

    public static ChatbotAnswerMessageBuilder copy(final ChatbotAnswerMessage chatbotAnswerMessage) {
        return Optional.ofNullable(chatbotAnswerMessage)
                .map(cam -> new ChatbotAnswerMessageBuilder()
                        .generated(cam.getGenerated())
                        .chatId(cam.getChatId())
                        .messageId(cam.getMessageId())
                        .answer(cam.getAnswer()))
                .orElse(null);
    }

    public ChatbotAnswerMessageBuilder answer(final String answer) {
        this.answer = answer;
        return this;
    }

    public ChatbotAnswerMessage build() {
        final ChatbotAnswerMessage result = new ChatbotAnswerMessage(generated, chatId, messageId);
        result.setAnswer(answer);
        
        return result;
    }
}