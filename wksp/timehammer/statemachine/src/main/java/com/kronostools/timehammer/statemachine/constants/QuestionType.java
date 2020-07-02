package com.kronostools.timehammer.statemachine.constants;

import com.kronostools.timehammer.common.messages.constants.ChatbotMessages;
import com.kronostools.timehammer.common.utils.CommonUtils;
import com.kronostools.timehammer.statemachine.model.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public enum QuestionType {
    QUESTION_WORK_START("QWS", ChatbotMessages.QUESTION_WORK_START, new ArrayList<>() {{
        add(new Answer(AnswerOption.START_WORK, ChatbotMessages.QUESTION_WORK_START_OPTION_Y));
        add(new Answer(AnswerOption.WAIT_5M, ChatbotMessages.ANSWER_N5M));
        add(new Answer(AnswerOption.WAIT_10M, ChatbotMessages.ANSWER_N10M));
        add(new Answer(AnswerOption.WAIT_15M, ChatbotMessages.ANSWER_N15M));
        add(new Answer(AnswerOption.WAIT_20M, ChatbotMessages.ANSWER_N20M));
        add(new Answer(AnswerOption.NO, ChatbotMessages.ANSWER_N));
    }}),
    QUESTION_WORK_END("QWE", ChatbotMessages.QUESTION_WORK_END, new ArrayList<>() {{
        add(new Answer(AnswerOption.END_WORK, ChatbotMessages.QUESTION_WORK_END_OPTION_Y));
        add(new Answer(AnswerOption.WAIT_5M, ChatbotMessages.ANSWER_N5M));
        add(new Answer(AnswerOption.WAIT_10M, ChatbotMessages.ANSWER_N10M));
        add(new Answer(AnswerOption.WAIT_15M, ChatbotMessages.ANSWER_N15M));
        add(new Answer(AnswerOption.WAIT_20M, ChatbotMessages.ANSWER_N20M));
    }}),
    QUESTION_LUNCH_START("QLS", ChatbotMessages.QUESTION_LUNCH_START, new ArrayList<>() {{
        add(new Answer(AnswerOption.START_LUNCH, ChatbotMessages.QUESTION_LUNCH_START_OPTION_Y));
        add(new Answer(AnswerOption.WAIT_5M, ChatbotMessages.ANSWER_N5M));
        add(new Answer(AnswerOption.WAIT_10M, ChatbotMessages.ANSWER_N10M));
        add(new Answer(AnswerOption.WAIT_15M, ChatbotMessages.ANSWER_N15M));
        add(new Answer(AnswerOption.WAIT_20M, ChatbotMessages.ANSWER_N20M));
        add(new Answer(AnswerOption.NO, ChatbotMessages.ANSWER_N));
    }}),
    QUESTION_LUNCH_END("QLE", ChatbotMessages.QUESTION_LUNCH_END, new ArrayList<>() {{
        add(new Answer(AnswerOption.END_LUNCH, ChatbotMessages.QUESTION_LUNCH_END_OPTION_Y));
        add(new Answer(AnswerOption.WAIT_5M, ChatbotMessages.ANSWER_N5M));
        add(new Answer(AnswerOption.WAIT_10M, ChatbotMessages.ANSWER_N10M));
        add(new Answer(AnswerOption.WAIT_15M, ChatbotMessages.ANSWER_N15M));
        add(new Answer(AnswerOption.WAIT_20M, ChatbotMessages.ANSWER_N20M));
    }});

    private final String code;
    private final String[] messages;
    private final List<Answer> options;

    QuestionType(final String code, final String[] messages, final List<Answer> options) {
        this.code = code;
        this.messages = messages;
        this.options = options;
    }

    public static QuestionType getFromCode(final String code) {
        return Optional.ofNullable(code)
                .flatMap(c -> Stream.of(QuestionType.values())
                        .filter(qt -> qt.getCode().equals(code))
                        .findFirst())
                .orElse(null);
    }

    public String getAnswer(final AnswerOption answerOption) {
        return options.stream()
                .filter(a -> a.getOption() == answerOption)
                .findFirst()
                .map(Answer::getSuccessfulMessage)
                .orElse(null);
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return CommonUtils.getRandomElementFromArray(messages);
    }

    public List<Answer> getOptions() {
        return options;
    }
}
