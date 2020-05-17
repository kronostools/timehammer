package com.kronostools.timehammer.common.messages;

import java.time.LocalDateTime;

public abstract class PlatformMessageBuilder<B> {
    protected LocalDateTime generated;

    public B generated(final LocalDateTime timestamp) {
        this.generated = timestamp;
        return (B) this;
    }
}
