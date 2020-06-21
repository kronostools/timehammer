package com.kronostools.timehammer.statemachine.model;

import java.time.LocalDateTime;

public class Wait {
    private final LocalDateTime limitTimestamp;

    public Wait(final LocalDateTime limitTimestamp) {
        this.limitTimestamp = limitTimestamp;
    }

    public LocalDateTime getLimitTimestamp() {
        return limitTimestamp;
    }

    public boolean isExpired(final LocalDateTime now) {
        return limitTimestamp.isBefore(now);
    }
}