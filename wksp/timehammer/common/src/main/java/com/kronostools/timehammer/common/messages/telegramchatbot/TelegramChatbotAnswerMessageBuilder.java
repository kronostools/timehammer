package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.constants.AnswerOption;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkerStatusPhase;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkerStatusPhaseBuilder;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class TelegramChatbotAnswerMessageBuilder extends TelegramChatbotMessageBuilder<TelegramChatbotAnswerMessageBuilder> {
    private String rawAnswer;
    private WorkerCurrentPreferencesPhase workerCurrentPreferencesPhase;
    private AnswerOption answerOption;
    private Company company;
    private UpdateWorkerStatusPhase updateWorkerStatusPhase;

    public static TelegramChatbotAnswerMessage copyAndBuild(final TelegramChatbotAnswerMessage telegramChatbotAnswerMessage) {
        return Optional.ofNullable(telegramChatbotAnswerMessage)
                .map(cam -> TelegramChatbotAnswerMessageBuilder.copy(cam).build())
                .orElse(null);
    }

    public static TelegramChatbotAnswerMessageBuilder copy(final TelegramChatbotAnswerMessage telegramChatbotAnswerMessage) {
        return Optional.ofNullable(telegramChatbotAnswerMessage)
                .map(tcam -> new TelegramChatbotAnswerMessageBuilder()
                        .generated(tcam.getGenerated())
                        .chatId(tcam.getChatId())
                        .messageId(tcam.getMessageId())
                        .rawAnswer(tcam.getRawAnswer())
                        .workerCurrentPreferencesPhase(WorkerCurrentPreferencesPhaseBuilder.copyAndBuild(tcam.getWorkerCurrentPreferencesPhase()))
                        .answerOption(tcam.getAnswerOption())
                        .company(tcam.getCompany())
                        .updateWorkerStatusPhase(UpdateWorkerStatusPhaseBuilder.copyAndBuild(tcam.getUpdateWorkerStatusPhase())))
                .orElse(null);
    }

    public TelegramChatbotAnswerMessageBuilder rawAnswer(final String rawAnswer) {
        this.rawAnswer = rawAnswer;
        return this;
    }

    public TelegramChatbotAnswerMessageBuilder workerCurrentPreferencesPhase(final WorkerCurrentPreferencesPhase workerCurrentPreferencesPhase) {
        this.workerCurrentPreferencesPhase = workerCurrentPreferencesPhase;
        return this;
    }

    public TelegramChatbotAnswerMessageBuilder answerOption(final AnswerOption answerOption) {
        this.answerOption = answerOption;
        return this;
    }

    public TelegramChatbotAnswerMessageBuilder company(final Company company) {
        this.company = company;
        return this;
    }

    public TelegramChatbotAnswerMessageBuilder updateWorkerStatusPhase(final UpdateWorkerStatusPhase updateWorkerStatusPhase) {
        this.updateWorkerStatusPhase = updateWorkerStatusPhase;
        return this;
    }

    public TelegramChatbotAnswerMessage build() {
        final TelegramChatbotAnswerMessage result = new TelegramChatbotAnswerMessage(generated, chatId, messageId);
        result.setRawAnswer(rawAnswer);
        result.setWorkerCurrentPreferencesPhase(workerCurrentPreferencesPhase);
        result.setAnswerOption(answerOption);
        result.setCompany(company);
        result.setUpdateWorkerStatusPhase(updateWorkerStatusPhase);

        return result;
    }
}