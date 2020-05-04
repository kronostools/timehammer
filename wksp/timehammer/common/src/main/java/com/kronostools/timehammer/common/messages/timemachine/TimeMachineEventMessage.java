package com.kronostools.timehammer.common.messages.timemachine;

import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.messages.PlatformMessage;

import java.time.LocalDateTime;

public class TimeMachineEventMessage extends PlatformMessage {
    private LocalDateTime newTimestamp;
    private SupportedTimezone timezone;

    public static class Builder {
        private LocalDateTime generated;
        private LocalDateTime newTimestamp;
        private SupportedTimezone timezone;

        Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public Builder generated(final LocalDateTime generated) {
            this.generated = generated;
            return this;
        }

        public Builder withNewTimestamp(final LocalDateTime newTimestamp) {
            this.newTimestamp = newTimestamp;
            return this;
        }

        public Builder atTimezone(final SupportedTimezone timezone) {
            this.timezone = timezone;
            return this;
        }

        public TimeMachineEventMessage build() {
            final TimeMachineEventMessage result = new TimeMachineEventMessage();
            result.setTimestamp(generated);
            result.setNewTimestamp(newTimestamp);
            result.setTimezone(timezone);

            return result;
        }
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