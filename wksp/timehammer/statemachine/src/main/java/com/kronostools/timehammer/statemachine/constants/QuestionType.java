package com.kronostools.timehammer.statemachine.constants;

import com.kronostools.timehammer.common.messages.constants.ChatbotMessages;
import com.kronostools.timehammer.common.messages.constants.StatusContext;
import com.kronostools.timehammer.common.messages.constants.StatusContextAction;
import com.kronostools.timehammer.common.utils.CommonUtils;

import java.util.Optional;
import java.util.stream.Stream;

public enum QuestionType {
    QUESTION_WORK_START("QWS", ChatbotMessages.QUESTION_WORK_START, StatusContext.WORK, StatusContextAction.START),
    QUESTION_WORK_END("QWE", ChatbotMessages.QUESTION_WORK_END, StatusContext.WORK, StatusContextAction.END),
    QUESTION_LUNCH_START("QLS", ChatbotMessages.QUESTION_LUNCH_START, StatusContext.LUNCH, StatusContextAction.START),
    QUESTION_LUNCH_END("QLE", ChatbotMessages.QUESTION_LUNCH_END, StatusContext.LUNCH, StatusContextAction.END);

    private final String code;
    private final String[] messages;
    private final StatusContext context;
    private final StatusContextAction contextAction;

    QuestionType(final String code, final String[] messages, final StatusContext context, final StatusContextAction contextAction) {
        this.code = code;
        this.messages = messages;
        this.context = context;
        this.contextAction = contextAction;
    }

    public static QuestionType getFromCode(final String code) {
        return Optional.ofNullable(code)
                .flatMap(c -> Stream.of(QuestionType.values())
                        .filter(qt -> qt.getCode().equals(code))
                        .findFirst())
                .orElse(null);
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return CommonUtils.getRandomElementFromArray(messages);
    }

    public StatusContext getContext() {
        return context;
    }

    public StatusContextAction getContextAction() {
        return contextAction;
    }
}
