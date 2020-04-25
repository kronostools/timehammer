package com.kronostools.timehammer.enums;

import com.kronostools.timehammer.service.TimeMachineService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.stream.Stream;

public enum SupportedTimezone {
    UTC("UTC"),
    EUROPE_MADRID("Europe/Madrid");

    private final String timezoneName;
    private final ZoneId zoneId;

    SupportedTimezone(final String timezoneName) {
        this.timezoneName = timezoneName;
        this.zoneId = ZoneId.of(timezoneName);
    }

    public static SupportedTimezone fromTimezoneName(String timezoneName) {
        return Stream.of(SupportedTimezone.values())
                .filter(tz -> tz.getTimezoneName().equals(timezoneName))
                .findFirst()
                .orElse(SupportedTimezone.UTC);
    }

    public String getTimezoneName() {
        return timezoneName;
    }

    public ZoneId getZone() {
        return zoneId;
    }

    public ZoneOffset getOffset(final LocalDate date) {
        return getOffset(TimeMachineService.atMidday(date));
    }

    public ZoneOffset getOffset(final LocalDateTime dateTime) {
        return zoneId.getRules().getOffset(dateTime);
    }
}