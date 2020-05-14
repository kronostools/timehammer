package com.kronostools.timehammer.common.messages.timemachine;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.messages.PlatformMessage;

import java.time.LocalDateTime;

@JsonDeserialize(builder = TimeMachineEventMessageBuilder.class)
public class TimeMachineEventMessage extends PlatformMessage {
    private LocalDateTime newTimestamp;
    private SupportedTimezone timezone;

    TimeMachineEventMessage(final LocalDateTime timestamp) {
        super(timestamp);
    }

    public LocalDateTime getNewTimestamp() {
        return newTimestamp;
    }

    public void setNewTimestamp(LocalDateTime newTimestamp) {
        this.newTimestamp = newTimestamp;
    }

    public SupportedTimezone getTimezone() {
        return timezone;
    }

    public void setTimezone(SupportedTimezone timezone) {
        this.timezone = timezone;
    }
}