package com.kronostools.timehammer.common.messages.constants;

import com.kronostools.timehammer.common.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public enum QuestionType {
    QUESTION_WORK_START(ChatbotMessages.QUESTION_WORK_START, new ArrayList<>() {{
        add(AnswerOption.START_WORK);
        add(AnswerOption.WAIT_5M);
        add(AnswerOption.WAIT_10M);
        add(AnswerOption.WAIT_15M);
        add(AnswerOption.WAIT_20M);
        add(AnswerOption.NO);
    }}),
    QUESTION_WORK_END(ChatbotMessages.QUESTION_WORK_END, new ArrayList<>() {{
        add(AnswerOption.END_WORK);
        add(AnswerOption.WAIT_5M);
        add(AnswerOption.WAIT_10M);
        add(AnswerOption.WAIT_15M);
        add(AnswerOption.WAIT_20M);
    }}),
    QUESTION_LUNCH_START(ChatbotMessages.QUESTION_LUNCH_START, new ArrayList<>() {{
        add(AnswerOption.START_LUNCH);
        add(AnswerOption.WAIT_5M);
        add(AnswerOption.WAIT_10M);
        add(AnswerOption.WAIT_15M);
        add(AnswerOption.WAIT_20M);
        add(AnswerOption.NO);
    }}),
    QUESTION_LUNCH_END(ChatbotMessages.QUESTION_LUNCH_END, new ArrayList<>() {{
        add(AnswerOption.END_LUNCH);
        add(AnswerOption.WAIT_5M);
        add(AnswerOption.WAIT_10M);
        add(AnswerOption.WAIT_15M);
        add(AnswerOption.WAIT_20M);
    }});

    private final String[] messages;
    private final List<AnswerOption> options;

    QuestionType(final String[] messages, final List<AnswerOption> options) {
        this.messages = messages;
        this.options = options;
    }

    public String getText() {
        return CommonUtils.getRandomElementFromArray(messages);
    }

    public List<AnswerOption> getOptions() {
        return options;
    }
}
