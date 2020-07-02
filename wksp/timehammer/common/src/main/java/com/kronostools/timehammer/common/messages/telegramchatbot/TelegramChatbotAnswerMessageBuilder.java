package com.kronostools.timehammer.common.messages.telegramchatbot;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class TelegramChatbotAnswerMessageBuilder extends TelegramChatbotMessageBuilder<TelegramChatbotAnswerMessageBuilder> {
    private String rawAnswer;
    private WorkerCurrentPreferencesPhase workerCurrentPreferencesPhase;
    private boolean wait;
    private String answerResponseText;

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
                        .wait(tcam.isWait())
                        .answerResponseText(tcam.getAnswerResponseText()))
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

    public TelegramChatbotAnswerMessageBuilder wait(final boolean wait) {
        this.wait = wait;
        return this;
    }

    public TelegramChatbotAnswerMessageBuilder answerResponseText(final String answerResponseText) {
        this.answerResponseText = answerResponseText;
        return this;
    }

    public TelegramChatbotAnswerMessage build() {
        final TelegramChatbotAnswerMessage result = new TelegramChatbotAnswerMessage(generated, chatId, messageId);
        result.setRawAnswer(rawAnswer);
        result.setWorkerCurrentPreferencesPhase(workerCurrentPreferencesPhase);
        result.setWait(wait);
        result.setAnswerResponseText(answerResponseText);

        return result;
    }
}