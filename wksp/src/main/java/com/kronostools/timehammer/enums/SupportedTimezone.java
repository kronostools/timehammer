package com.kronostools.timehammer.enums;

import java.time.ZoneId;

public enum SupportedTimezone {
    UTC("UTC"),
    EUROPE_MADRID("Europe/Madrid");

    private final String timezoneName;
    private final ZoneId zoneId;

    SupportedTimezone(final String timezoneName) {
        this.timezoneName = timezoneName;
        this.zoneId = ZoneId.of(timezoneName);
    }

    public String getTimezoneName() {
        return timezoneName;
    }

    public ZoneId getZoneId() {
        return zoneId;
    }
}