package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

@JsonDeserialize(builder = TelegramChatbotAnswerMessageBuilder.class)
public class TelegramChatbotAnswerMessage extends TelegramChatbotInputMessage {
    private String answer;

    TelegramChatbotAnswerMessage(final LocalDateTime generated, final String chatId, final Long messageId) {
        super(generated, chatId, messageId);
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}