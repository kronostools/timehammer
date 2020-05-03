package com.kronostools.timehammer.common.messages;

import java.time.LocalDateTime;

public abstract class PlatformMessage {
    protected LocalDateTime timestamp;

    protected PlatformMessage() {}

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}