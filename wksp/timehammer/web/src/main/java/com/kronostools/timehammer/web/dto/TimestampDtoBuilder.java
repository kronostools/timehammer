package com.kronostools.timehammer.web.dto;

public class TimestampDtoBuilder {
    private String timestamp;

    public TimestampDtoBuilder timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public TimestampDto build() {
        TimestampDto result = new TimestampDto();
        result.setTimestamp(timestamp);

        return result;
    }
}