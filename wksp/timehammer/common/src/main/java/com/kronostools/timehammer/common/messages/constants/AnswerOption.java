package com.kronostools.timehammer.common.messages.constants;

import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum AnswerOption {
    SW_Y("SWY", ChatbotMessages.ANSWER_Y_BUTTON, false, StatusContext.WORK, StatusContextAction.START, ChatbotMessages.SW_RESPONSE_Y),
    SW_W5M("SWW5M", ChatbotMessages.ANSWER_N5M_BUTTON, true, StatusContext.WORK, StatusContextAction.START, ChatbotMessages.RESPONSE_W5M),
    SW_W10M("SWW10M", ChatbotMessages.ANSWER_N10M_BUTTON, true, StatusContext.WORK, StatusContextAction.START, ChatbotMessages.RESPONSE_W10M),
    SW_W15M("SWW15M", ChatbotMessages.ANSWER_N15M_BUTTON, true, StatusContext.WORK, StatusContextAction.START, ChatbotMessages.RESPONSE_W15M),
    SW_W20M("SWW20M", ChatbotMessages.ANSWER_N20M_BUTTON, true, StatusContext.WORK, StatusContextAction.START, ChatbotMessages.RESPONSE_W20M),
    SW_N("SWN", ChatbotMessages.ANSWER_N_BUTTON, true, StatusContext.WORK, StatusContextAction.START, ChatbotMessages.RESPONSE_N),

    EW_Y("EWY", ChatbotMessages.ANSWER_Y_BUTTON, false, StatusContext.WORK, StatusContextAction.END, ChatbotMessages.EW_RESPONSE_Y),
    EW_W5M("EWW5M", ChatbotMessages.ANSWER_N5M_BUTTON, true, StatusContext.WORK, StatusContextAction.END, ChatbotMessages.RESPONSE_W5M),
    EW_W10M("EWW10M", ChatbotMessages.ANSWER_N10M_BUTTON, true, StatusContext.WORK, StatusContextAction.END, ChatbotMessages.RESPONSE_W10M),
    EW_W15M("EWW15M", ChatbotMessages.ANSWER_N15M_BUTTON, true, StatusContext.WORK, StatusContextAction.END, ChatbotMessages.RESPONSE_W15M),
    EW_W20M("EWW20M", ChatbotMessages.ANSWER_N20M_BUTTON, true, StatusContext.WORK, StatusContextAction.END, ChatbotMessages.RESPONSE_W20M),

    SL_Y("SLY", ChatbotMessages.ANSWER_Y_BUTTON, false, StatusContext.LUNCH, StatusContextAction.START, ChatbotMessages.SL_RESPONSE_Y),
    SL_W5M("SLW5M", ChatbotMessages.ANSWER_N5M_BUTTON, true, StatusContext.LUNCH, StatusContextAction.START, ChatbotMessages.RESPONSE_W5M),
    SL_W10M("SLW10M", ChatbotMessages.ANSWER_N10M_BUTTON, true, StatusContext.LUNCH, StatusContextAction.START, ChatbotMessages.RESPONSE_W10M),
    SL_W15M("SLW15M", ChatbotMessages.ANSWER_N15M_BUTTON, true, StatusContext.LUNCH, StatusContextAction.START, ChatbotMessages.RESPONSE_W15M),
    SL_W20M("SLW20M", ChatbotMessages.ANSWER_N20M_BUTTON, true, StatusContext.LUNCH, StatusContextAction.START, ChatbotMessages.RESPONSE_W20M),
    SL_N("SLN", ChatbotMessages.ANSWER_N_BUTTON, true, StatusContext.LUNCH, StatusContextAction.START, ChatbotMessages.RESPONSE_N),

    EL_Y("ELY", ChatbotMessages.ANSWER_Y_BUTTON, false, StatusContext.LUNCH, StatusContextAction.END, ChatbotMessages.EL_RESPONSE_Y),
    EL_W5M("ELW5M", ChatbotMessages.ANSWER_N5M_BUTTON, true, StatusContext.LUNCH, StatusContextAction.END, ChatbotMessages.RESPONSE_W5M),
    EL_W10M("ELW10M", ChatbotMessages.ANSWER_N10M_BUTTON, true, StatusContext.LUNCH, StatusContextAction.END, ChatbotMessages.RESPONSE_W10M),
    EL_W15M("ELW15M", ChatbotMessages.ANSWER_N15M_BUTTON, true, StatusContext.LUNCH, StatusContextAction.END, ChatbotMessages.RESPONSE_W15M),
    EL_W20M("ELW20M", ChatbotMessages.ANSWER_N20M_BUTTON, true, StatusContext.LUNCH, StatusContextAction.END, ChatbotMessages.RESPONSE_W20M);

    private final String code;
    private final String buttonText;
    private final boolean wait;
    private final StatusContext context;
    private final StatusContextAction contextAction;
    private final String responseText;

    AnswerOption(final String code, final String buttonText, final boolean wait, final StatusContext context, final StatusContextAction contextAction, final String responseText) {
        this.code = code;
        this.buttonText = buttonText;
        this.wait = wait;
        this.context = context;
        this.contextAction = contextAction;
        this.responseText = responseText;
    }

    public static AnswerOption getFromCode(final String code) {
        return Optional.ofNullable(code)
                .flatMap(c -> Stream.of(AnswerOption.values())
                        .filter(ao -> ao.getCode().equals(code))
                        .findFirst())
                .orElse(null);
    }

    public static List<AnswerOption> getAnswerOptions(final StatusContext context, final StatusContextAction contextAction) {
        return Stream.of(AnswerOption.values())
                .filter(ao -> ao.getContext() == context && ao.getContextAction() == contextAction)
                .collect(Collectors.toList());
    }

    public LocalDateTime getWaitLimitTimestamp(final LocalDateTime now) {
        final LocalDateTime waitLimitTimestamp;

        switch(this) {
            case SW_W5M:
            case EW_W5M:
            case SL_W5M:
            case EL_W5M:
                waitLimitTimestamp = now.plusMinutes(5);
                break;
            case SW_W10M:
            case EW_W10M:
            case SL_W10M:
            case EL_W10M:
                waitLimitTimestamp = now.plusMinutes(10);
                break;
            case SW_W15M:
            case EW_W15M:
            case SL_W15M:
            case EL_W15M:
                waitLimitTimestamp = now.plusMinutes(15);
                break;
            case SW_W20M:
            case EW_W20M:
            case SL_W20M:
            case EL_W20M:
                waitLimitTimestamp = now.plusMinutes(20);
                break;
            case SW_N:
            case SL_N:
                waitLimitTimestamp = CommonDateTimeUtils.atMidnight(now.toLocalDate());
                break;
            default:
                waitLimitTimestamp = null;
                break;
        }

        return waitLimitTimestamp;
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

    public StatusContext getContext() {
        return context;
    }

    public StatusContextAction getContextAction() {
        return contextAction;
    }

    public String getResponseText() {
        return responseText;
    }
}