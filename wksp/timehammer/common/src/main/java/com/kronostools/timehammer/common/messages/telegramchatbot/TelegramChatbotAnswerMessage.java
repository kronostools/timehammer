package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.constants.AnswerOption;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkerStatusPhase;

import java.time.LocalDateTime;

@JsonDeserialize(builder = TelegramChatbotAnswerMessageBuilder.class)
public class TelegramChatbotAnswerMessage extends TelegramChatbotMessage {
    private String rawAnswer;
    private WorkerCurrentPreferencesPhase workerCurrentPreferencesPhase;
    private AnswerOption answerOption;
    private Company company;
    private UpdateWorkerStatusPhase updateWorkerStatusPhase;

    TelegramChatbotAnswerMessage(final LocalDateTime generated, final String chatId, final Long messageId) {
        super(generated, chatId, messageId);
    }

    @JsonIgnore
    public boolean isWait() {
        return answerOption.isWait();
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

    public AnswerOption getAnswerOption() {
        return answerOption;
    }

    public void setAnswerOption(AnswerOption answerOption) {
        this.answerOption = answerOption;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public UpdateWorkerStatusPhase getUpdateWorkerStatusPhase() {
        return updateWorkerStatusPhase;
    }

    public void setUpdateWorkerStatusPhase(UpdateWorkerStatusPhase updateWorkerStatusPhase) {
        this.updateWorkerStatusPhase = updateWorkerStatusPhase;
    }
}