package com.kronostools.timehammer.web.dto;

public class TimestampDtoBuilder {
    private String timestamp;
    private String timezone;

    public TimestampDtoBuilder timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public TimestampDtoBuilder timezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    public TimestampDto build() {
        TimestampDto result = new TimestampDto();
        result.setTimestamp(timestamp);
        result.setTimezone(timezone);

        return result;
    }
}