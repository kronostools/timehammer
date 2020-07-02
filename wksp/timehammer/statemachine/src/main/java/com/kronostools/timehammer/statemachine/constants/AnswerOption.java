package com.kronostools.timehammer.statemachine.constants;

import com.kronostools.timehammer.common.messages.constants.ChatbotMessages;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

public enum AnswerOption {
    START_WORK("SW", ChatbotMessages.ANSWER_Y_BUTTON, false),
    END_WORK("EW", ChatbotMessages.ANSWER_Y_BUTTON, false),
    START_LUNCH("SL", ChatbotMessages.ANSWER_Y_BUTTON, false),
    END_LUNCH("EL", ChatbotMessages.ANSWER_Y_BUTTON, false),
    WAIT_5M("W5M", ChatbotMessages.ANSWER_N5M_BUTTON, true),
    WAIT_10M("W10M", ChatbotMessages.ANSWER_N10M_BUTTON, true),
    WAIT_15M("W15M", ChatbotMessages.ANSWER_N15M_BUTTON, true),
    WAIT_20M("W20M", ChatbotMessages.ANSWER_N20M_BUTTON, true),
    NO("NO", ChatbotMessages.ANSWER_N_BUTTON, true);

    private final String code;
    private final boolean wait;
    private final String buttonText;

    AnswerOption(final String code, final String buttonText, final boolean wait) {
        this.code = code;
        this.buttonText = buttonText;
        this.wait = wait;
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

    public boolean isWait() {
        return wait;
    }

    public LocalDateTime getWaitLimitTimestamp(final LocalDateTime now) {
        final LocalDateTime waitLimitTimestamp;

        switch(this) {
            case WAIT_5M:
                waitLimitTimestamp = now.plusMinutes(5);
                break;
            case WAIT_10M:
                waitLimitTimestamp = now.plusMinutes(10);
                break;
            case WAIT_15M:
                waitLimitTimestamp = now.plusMinutes(15);
                break;
            case WAIT_20M:
                waitLimitTimestamp = now.plusMinutes(20);
                break;
            case NO:
                waitLimitTimestamp = CommonDateTimeUtils.atMidnight(now.toLocalDate());
                break;
            default:
                waitLimitTimestamp = null;
                break;
        }

        return waitLimitTimestamp;
    }
}