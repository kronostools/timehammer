package com.kronostools.timehammer.common.messages.timemachine;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.messages.PlatformMessageBuilder;

import java.time.LocalDateTime;

@JsonPOJOBuilder(withPrefix = "")
public class TimeMachineEventMessageBuilder extends PlatformMessageBuilder<TimeMachineEventMessageBuilder> {
    private LocalDateTime newTimestamp;
    private SupportedTimezone timezone;

    public static TimeMachineEventMessageBuilder copy(final TimeMachineEventMessage timeMachineEventMessage) {
        return new TimeMachineEventMessageBuilder();
    }

    public TimeMachineEventMessageBuilder newTimestamp(final LocalDateTime newTimestamp) {
        this.newTimestamp = newTimestamp;
        return this;
    }

    public TimeMachineEventMessageBuilder timezone(final SupportedTimezone timezone) {
        this.timezone = timezone;
        return this;
    }

    public TimeMachineEventMessage build() {
        final TimeMachineEventMessage result = new TimeMachineEventMessage(timestamp);
        result.setNewTimestamp(newTimestamp);
        result.setTimezone(timezone);

        return result;
    }
}