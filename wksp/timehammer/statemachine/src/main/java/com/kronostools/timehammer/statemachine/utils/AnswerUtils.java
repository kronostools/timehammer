package com.kronostools.timehammer.statemachine.utils;

import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.utils.CommonUtils;
import com.kronostools.timehammer.statemachine.constants.AnswerOption;
import com.kronostools.timehammer.statemachine.constants.QuestionType;

public class AnswerUtils {
    private final static String SEPARATOR = "#";

    public static String getAnswerCode(final QuestionType questionType, final AnswerOption answerOption, final Company company) {
        return CommonUtils.stringFormat("{}{}{}{}{}", questionType.getCode(), SEPARATOR, answerOption.getCode(), SEPARATOR, company.getCode());
    }

    public static QuestionType getQuestionType(final String rawAnswer) {
        return QuestionType.getFromCode(rawAnswer.split(SEPARATOR)[0]);
    }

    public static AnswerOption getAnswerOption(final String rawAnswer) {
        return AnswerOption.getFromCode(rawAnswer.split(SEPARATOR)[1]);
    }

    public static Company getCompany(final String rawAnswer) {
        return Company.fromCode(rawAnswer.split(SEPARATOR)[2]);
    }
}