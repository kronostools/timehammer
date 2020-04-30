package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.enums.AnswerType;

public class AnswerOptionVo {
    private final AnswerType answerType;
    private final String answerResponseText;

    public AnswerOptionVo(final AnswerType answerType) {
        this.answerType = answerType;
        this.answerResponseText = answerType.getDefaultAnswerResponseText();
    }

    public AnswerOptionVo(final AnswerType answerType, final String answerResponseText) {
        this.answerType = answerType;
        this.answerResponseText = answerResponseText;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }

    public String getAnswerResponseText() {
        return answerResponseText;
    }

    @Override
    public String toString() {
        return "AnswerOptionVo{" +
                "answerType='" + answerType.getAnswerCode() + '\'' +
                ", answerResponseText='" + answerResponseText + '\'' +
                '}';
    }
}
