package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.constants.Company;

import java.time.LocalDateTime;

@JsonDeserialize(builder = TelegramChatbotAnswerMessageBuilder.class)
public class TelegramChatbotAnswerMessage extends TelegramChatbotMessage {
    private String rawAnswer;
    private WorkerCurrentPreferencesPhase workerCurrentPreferencesPhase;
    private boolean wait;
    private Company company;
    private String answerResponseText;

    TelegramChatbotAnswerMessage(final LocalDateTime generated, final String chatId, final Long messageId) {
        super(generated, chatId, messageId);
    }

    public String getRawAnswer() {
        return rawAnswer;
    }

    public void setRawAnswer(String rawAnswer) {
        this.rawAnswer = rawAnswer;
    }

    public WorkerCurrentPreferencesPhase getWorkerCurrentPreferencesPhase() {
        return workerCurrentPreferencesPhase;
    }

    public void setWorkerCurrentPreferencesPhase(WorkerCurrentPreferencesPhase workerCurrentPreferencesPhase) {
        this.workerCurrentPreferencesPhase = workerCurrentPreferencesPhase;
    }

    public boolean isWait() {
        return wait;
    }

    public void setWait(boolean wait) {
        this.wait = wait;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getAnswerResponseText() {
        return answerResponseText;
    }

    public void setAnswerResponseText(String answerResponseText) {
        this.answerResponseText = answerResponseText;
    }
}