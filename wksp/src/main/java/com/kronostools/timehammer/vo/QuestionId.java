package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.enums.QuestionType;

import java.util.Objects;

public class QuestionId {
    private final String chatId;
    private final QuestionType questionType;

    public QuestionId(final String chatId, final QuestionType questionType) {
        this.chatId = chatId;
        this.questionType = questionType;
    }

    public String getChatId() {
        return chatId;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionId that = (QuestionId) o;
        return chatId.equals(that.chatId) &&
                questionType == that.questionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, questionType);
    }

    @Override
    public String toString() {
        return "QuestionId{" +
                "externalWorkerId='" + chatId + '\'' +
                ", questionType=" + questionType.getQuestionCode() +
                '}';
    }
}