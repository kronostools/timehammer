package com.kronostools.timehammer.common.messages;

import java.time.LocalDateTime;

public abstract class PlatformMessage {
    protected final LocalDateTime timestamp;

    protected PlatformMessage(final LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}