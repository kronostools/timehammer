package com.kronostools.timehammer.enums;

import com.kronostools.timehammer.utils.ChatbotMessages;

import java.util.Optional;
import java.util.stream.Stream;

public enum AnswerType {
    Y("Y", ChatbotMessages.ANSWER_Y_BUTTON, ChatbotMessages.ANSWER_Y),
    N5M("+5M", ChatbotMessages.ANSWER_N5M_BUTTON, ChatbotMessages.ANSWER_N5M),
    N10M("+10M", ChatbotMessages.ANSWER_N10M_BUTTON, ChatbotMessages.ANSWER_N10M),
    N15M("+15M", ChatbotMessages.ANSWER_N15M_BUTTON, ChatbotMessages.ANSWER_N15M),
    N20M("+20M", ChatbotMessages.ANSWER_N20M_BUTTON, ChatbotMessages.ANSWER_N20M),
    N("N", ChatbotMessages.ANSWER_N_BUTTON, ChatbotMessages.ANSWER_N);

    private final String answerCode;
    private final String answerButtonText;
    private final String defaultAnswerMessage;

    AnswerType(final String answerCode, final String answerButtonText, final String defaultAnswerMessage) {
        this.answerCode = answerCode;
        this.answerButtonText = answerButtonText;
        this.defaultAnswerMessage = defaultAnswerMessage;
    }

    public String getAnswerCode() {
        return answerCode;
    }

    public String getAnswerButtonText() {
        return answerButtonText;
    }

    public String getDefaultAnswerResponseText() {
        return defaultAnswerMessage;
    }

    public static Optional<AnswerType> fromAnswerCode(final String answerCode) {
        return Stream.of(AnswerType.values())
                .filter(c -> c.getAnswerCode().equals(answerCode))
                .findFirst();
    }
}