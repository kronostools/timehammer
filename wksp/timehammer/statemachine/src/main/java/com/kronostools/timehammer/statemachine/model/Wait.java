package com.kronostools.timehammer.statemachine.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Wait implements Serializable {
    private final LocalDateTime limitTimestamp;
    private final boolean allDay;

    public Wait(final LocalDateTime limitTimestamp, final boolean allDay) {
        this.limitTimestamp = limitTimestamp;
        this.allDay = allDay;
    }

    public LocalDateTime getLimitTimestamp() {
        return limitTimestamp;
    }

    public boolean isExpired(final LocalDateTime now) {
        return limitTimestamp.isBefore(now);
    }

    public boolean isAllDay() {
        return allDay;
    }
}