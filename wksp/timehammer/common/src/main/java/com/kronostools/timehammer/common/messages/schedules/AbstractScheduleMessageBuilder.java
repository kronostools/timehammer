package com.kronostools.timehammer.common.messages.schedules;

import com.kronostools.timehammer.common.messages.PlatformMessageBuilder;

import java.util.UUID;

public abstract class AbstractScheduleMessageBuilder<B> extends PlatformMessageBuilder<B> {
    protected String name;
    protected UUID executionId;

    public B name(final String name) {
        this.name = name;
        return (B) this;
    }

    public B executionId(final UUID executionId) {
        this.executionId = executionId;
        return (B) this;
    }
}