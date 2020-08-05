package com.kronostools.timehammer.statemachine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class Wait {
    private LocalDateTime limitTimestamp;
    private boolean allDay;

    public Wait() {}

    public Wait(final LocalDateTime limitTimestamp, final boolean allDay) {
        this.limitTimestamp = limitTimestamp;
        this.allDay = allDay;
    }

    public LocalDateTime getLimitTimestamp() {
        return limitTimestamp;
    }

    public void setLimitTimestamp(LocalDateTime limitTimestamp) {
        this.limitTimestamp = limitTimestamp;
    }

    @JsonIgnore
    public boolean isExpired(final LocalDateTime now) {
        return limitTimestamp.isBefore(now);
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }
}