package com.kronostools.timehammer.statemachine.model;

import com.kronostools.timehammer.common.messages.constants.AnswerOption;

public class Answer {
    private final AnswerOption option;
    private final String successfulMessage;

    public Answer(final AnswerOption option, final String successfulMessage) {
        this.option = option;
        this.successfulMessage = successfulMessage;
    }

    public AnswerOption getOption() {
        return option;
    }

    public String getSuccessfulMessage() {
        return successfulMessage;
    }
}