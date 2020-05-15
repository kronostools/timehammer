package com.kronostools.timehammer.web.dto;

public class TimestampDto extends Dto {
    private String timestamp;
    private String timezone;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}