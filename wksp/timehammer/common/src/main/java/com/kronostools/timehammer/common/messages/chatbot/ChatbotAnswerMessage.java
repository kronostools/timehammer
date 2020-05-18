package com.kronostools.timehammer.common.messages.chatbot;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

@JsonDeserialize(builder = ChatbotAnswerMessageBuilder.class)
public class ChatbotAnswerMessage extends ChatbotInputMessage {
    private String answer;

    ChatbotAnswerMessage(final LocalDateTime generated, final String chatId, final Long messageId) {
        super(generated, chatId, messageId);
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}