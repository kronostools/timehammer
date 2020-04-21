package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.enums.AnswerType;
import com.kronostools.timehammer.service.TimeMachineService;

import java.time.LocalDateTime;

public class QuestionVo {
    private final QuestionId questionId;
    private final LocalDateTime timestamp;
    private final Long messageId;
    private AnswerType answer;
    private LocalDateTime waitUntil;

    public QuestionVo(final QuestionId questionId, final LocalDateTime timestamp, final Long messageId) {
        this.questionId = questionId;
        this.timestamp = timestamp;
        this.messageId = messageId;
    }

    public QuestionId getQuestionId() {
        return questionId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Long getMessageId() {
        return messageId;
    }

    public AnswerType getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerType answer) {
        this.answer = answer;

        switch (answer) {
            case N5M:
                waitUntil = timestamp.plusMinutes(5L);
                break;
            case N10M:
                waitUntil = timestamp.plusMinutes(10L);
                break;
            case N15M:
                waitUntil = timestamp.plusMinutes(15L);
                break;
            case N20M:
                waitUntil = timestamp.plusMinutes(20L);
                break;
            case N:
                waitUntil = timestamp.plusDays(1L).withHour(0).withMinute(0).withSecond(0).withNano(0);
                break;
        }
    }

    public Boolean isNotAnswered() {
        return answer == null;
    }

    public Boolean isExpired(final LocalDateTime timestamp) {
        Boolean expired = Boolean.FALSE;

        if (waitUntil == null || waitUntil.isBefore(timestamp)) {
            expired = Boolean.TRUE;
        }

        return expired;
    }

    @Override
    public String toString() {
        return "QuestionVo{" +
                "questionId=" + questionId +
                ", timestamp=" + TimeMachineService.formatDateTimeFull(timestamp) +
                ", messageId=" + messageId +
                ", answer=" + answer +
                ", waitUntil=" + TimeMachineService.formatDateTimeFull(waitUntil) +
                '}';
    }
}