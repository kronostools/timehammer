package com.kronostools.timehammer.web.dto;

public class TimestampDto {
    private String timestamp;
    private String timezone;

    public static class Builder {
        private String timestamp;
        private String timezone;

        Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder timezone(String timezone) {
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