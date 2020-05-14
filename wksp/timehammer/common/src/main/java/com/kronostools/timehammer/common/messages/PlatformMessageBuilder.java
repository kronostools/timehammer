package com.kronostools.timehammer.common.messages;

import java.time.LocalDateTime;

public abstract class PlatformMessageBuilder<B> {
    protected LocalDateTime timestamp;

    public B timestamp(final LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return (B) this;
    }
}
