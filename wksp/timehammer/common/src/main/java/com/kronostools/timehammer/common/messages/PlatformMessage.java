package com.kronostools.timehammer.common.messages;

import java.time.LocalDateTime;

public abstract class PlatformMessage {
    protected final LocalDateTime generated;

    protected PlatformMessage(final LocalDateTime generated) {
        this.generated = generated;
    }

    public LocalDateTime getGenerated() {
        return generated;
    }
}