package com.kronostools.timehammer.statemachine.utils;

import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.constants.AnswerOption;
import com.kronostools.timehammer.common.utils.CommonUtils;

public class AnswerUtils {
    private final static String SEPARATOR = "#";

    public static String getAnswerCode(final AnswerOption answerOption, final Company company) {
        return CommonUtils.stringFormat("{}{}{}", answerOption.getCode(), SEPARATOR, company.getCode());
    }

    public static AnswerOption getAnswerOption(final String rawAnswer) {
        return AnswerOption.getFromCode(rawAnswer.split(SEPARATOR)[1]);
    }

    public static Company getCompany(final String rawAnswer) {
        return Company.fromCode(rawAnswer.split(SEPARATOR)[2]);
    }
}