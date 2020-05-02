package com.kronostools.timehammer.common.messages.schedules;

import com.kronostools.timehammer.common.messages.PlatformMessage;

import java.time.LocalDateTime;

public class ScheduleTriggerMessage extends PlatformMessage {
    private String name;

    public ScheduleTriggerMessage() {}

    public ScheduleTriggerMessage(final LocalDateTime timestamp, final String name) {
        super(timestamp);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}