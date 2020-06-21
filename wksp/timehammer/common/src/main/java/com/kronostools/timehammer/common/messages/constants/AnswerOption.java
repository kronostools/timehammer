package com.kronostools.timehammer.common.messages.constants;

import java.util.Optional;
import java.util.stream.Stream;

public enum AnswerOption {
    START_WORK("SW", ChatbotMessages.ANSWER_Y_BUTTON, ChatbotMessages.QUESTION_WORK_START_OPTION_Y),
    END_WORK("EW", ChatbotMessages.ANSWER_Y_BUTTON, ChatbotMessages.QUESTION_WORK_END_OPTION_Y),
    START_LUNCH("SL", ChatbotMessages.ANSWER_Y_BUTTON, ChatbotMessages.QUESTION_LUNCH_START_OPTION_Y),
    END_LUNCH("EL", ChatbotMessages.ANSWER_Y_BUTTON, ChatbotMessages.QUESTION_LUNCH_END_OPTION_Y),
    WAIT_5M("W5M", ChatbotMessages.ANSWER_N5M_BUTTON, ChatbotMessages.ANSWER_N5M),
    WAIT_10M("W10M", ChatbotMessages.ANSWER_N10M_BUTTON, ChatbotMessages.ANSWER_N10M),
    WAIT_15M("W15M", ChatbotMessages.ANSWER_N15M_BUTTON, ChatbotMessages.ANSWER_N15M),
    WAIT_20M("W20M", ChatbotMessages.ANSWER_N20M_BUTTON, ChatbotMessages.ANSWER_N20M),
    NO("NO", ChatbotMessages.ANSWER_N_BUTTON, ChatbotMessages.ANSWER_N);

    private final String code;
    private final String buttonText;
    private final String confirmationText;

    AnswerOption(final String code, final String buttonText, final String confirmationText) {
        this.code = code;
        this.buttonText = buttonText;
        this.confirmationText = confirmationText;
    }

    public static AnswerOption getFromCode(final String code) {
        return Optional.ofNullable(code)
                .flatMap(c -> Stream.of(AnswerOption.values())
                        .filter(ao -> ao.getCode().equals(code))
                        .findFirst())
                .orElse(null);
    }

    public String getCode() {
        return code;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getConfirmationText() {
        return confirmationText;
    }
}