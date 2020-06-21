package com.kronostools.timehammer.statemachine.model;

import com.kronostools.timehammer.common.messages.constants.QuestionType;

import java.util.Objects;

public class WaitId {
    private final String workerInternalId;
    private final QuestionType questionType;

    public WaitId(final String workerInternalId, final QuestionType questionType) {
        this.workerInternalId = workerInternalId;
        this.questionType = questionType;
    }

    public String getWorkerInternalId() {
        return workerInternalId;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaitId waitId = (WaitId) o;
        return workerInternalId.equals(waitId.workerInternalId) &&
                questionType == waitId.questionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(workerInternalId, questionType);
    }
}