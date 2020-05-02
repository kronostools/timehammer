package com.kronostools.timehammer.common.messages;

import java.time.LocalDateTime;

public class ScheduleTriggerMessage extends PlatformMessage {
    private final String name;

    public ScheduleTriggerMessage(final LocalDateTime timestamp, final String name) {
        super(timestamp);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}