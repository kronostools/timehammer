package com.kronostools.timehammer.service;

import com.kronostools.timehammer.config.TimehammerConfig;
import com.kronostools.timehammer.enums.SupportedTimezone;

import javax.enterprise.context.ApplicationScoped;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@ApplicationScoped
public class TimeMachineService {
    private static final String TIMEINTERVAL_SEPARATOR = ";";

    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String FORMAT_YYYYMMDDTHHMM = "yyyyMMdd'T'HH:mm";
    public static final String FORMAT_YYYYMMDDTHHMMSSSSS = "yyyyMMdd'T'HH:mm:ss.SSS";
    public static final String FORMAT_DDMMYYYY_SEP_FWS = "dd/MM/yyyy";
    public static final String FORMAT_HHMM = "HH:mm";
    public static final String FORMAT_HHMMSSSSS = "HH:mm:ss.SSS";

    public static final DateTimeFormatter FORMATTER_HHMM = DateTimeFormatter.ofPattern(FORMAT_HHMM);

    private LocalDateTime now;

    private final TimehammerConfig timehammerConfig;

    public TimeMachineService(final TimehammerConfig timehammerConfig) {
        this.now = now();
        this.timehammerConfig = timehammerConfig;
    }

    public void timeTravelToDateTimeWithZone(final LocalDateTime newTimestamp, final String zone) {
        now = newTimestamp.atOffset(getZoneOffsetFromDateTimeAtZone(newTimestamp, zone)).withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    public LocalDateTime getNow() {
        if (isTimeMachineMocked()) {
            return now;
        } else {
            return now();
        }
    }

    public LocalDateTime getNowAtZone(final String zone) {
        LocalDateTime now = getNow();

        return now.atOffset(ZoneOffset.UTC).withOffsetSameInstant(getZoneOffsetFromDateTimeAtZone(now, zone)).toLocalDateTime();
    }

    public LocalDateTime getTodayAtMidnight() {
        return getNow().withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    public static LocalDateTime parseDateTimeWithZone(final String[] dateTimeParts, final String[] dateTimeFormat, final String zone) {
        final LocalDate dateAtTimezone = LocalDate.parse(dateTimeParts[0], DateTimeFormatter.ofPattern(dateTimeFormat[0]));
        final LocalTime timeAtTimezone = LocalTime.parse(dateTimeParts[1], DateTimeFormatter.ofPattern(dateTimeFormat[1]));

        return ZonedDateTime
                .of(dateAtTimezone, timeAtTimezone, ZoneId.of(zone))
                .withZoneSameInstant(SupportedTimezone.UTC.getZoneId())
                .toLocalDateTime();
    }

    public static LocalDate parseDate(final String date, String dateFormat) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat));
    }

    public static LocalTime getTimeWithOffset(final int hour, final int minute, final ZoneOffset offset) {
        return LocalTime.of(hour, minute, 0, 0).atOffset(offset).withOffsetSameInstant(ZoneOffset.UTC).toLocalTime();
    }

    public static LocalTime getTimeWithOffset(final LocalTime time, final ZoneOffset offset) {
        return Optional.ofNullable(time)
                .map(t -> t.atOffset(offset)
                        .withOffsetSameInstant(ZoneOffset.UTC)
                        .toLocalTime())
                .orElse(null);
    }

    public static LocalDateTime getDateTimeAtZone(final LocalDateTime dateTime, final String zone) {
        return Optional.ofNullable(dateTime)
                .map(dt -> dateTime.atOffset(ZoneOffset.UTC)
                            .withOffsetSameInstant(getZoneOffsetFromDateTimeAtZone(dateTime, zone))
                            .toLocalDateTime())
                .orElse(null);
    }

    public static LocalDateTime parseDateTime(final String dateTime, final String dateTimeFormat) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(dateTimeFormat));
    }

    public static LocalTime parseTime(final String time, final String timeFormat) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern(timeFormat));
    }

    public static LocalTime parseTimeSimple(final String time) {
        return parseTime(time, FORMAT_HHMM);
    }

    public static LocalTime getStartFromTimeInterval(final String timeInterval) {
        return Optional.ofNullable(timeInterval)
                .map(ti -> {
                    final String rawStart = ti.substring(0, ti.indexOf(TIMEINTERVAL_SEPARATOR));

                    return parseTimeSimple(rawStart);
                })
                .orElse(null);
    }

    public static LocalTime getEndFromTimeInterval(final String timeInterval) {
        return Optional.ofNullable(timeInterval)
                .map(ti -> {
                    final String rawEnd = ti.substring(ti.indexOf(TIMEINTERVAL_SEPARATOR) + 1);

                    return parseTimeSimple(rawEnd);
                })
                .orElse(null);
    }

    public static String formatDateTime(final LocalDateTime dateTime, final String format) {
        return Optional.ofNullable(dateTime)
                .map(dt -> dt.format(DateTimeFormatter.ofPattern(format)))
                .orElse("");
    }

    public static String formatDateTimeFull(final LocalDateTime dateTime) {
        return formatDateTime(dateTime, FORMAT_YYYYMMDDTHHMMSSSSS);
    }

    public static String formatDate(final LocalDate date, final String dateFormat) {
        return Optional.ofNullable(date)
                .map(d -> d.format(DateTimeFormatter.ofPattern(dateFormat)))
                .orElse("");
    }

    public static String formatDate(final LocalDate date) {
        return formatDate(date, FORMAT_YYYYMMDD);
    }

    public static String formatTime(final LocalTime time, final String timeFormat) {
        return Optional.ofNullable(time)
                .map(t -> t.format(DateTimeFormatter.ofPattern(timeFormat)))
                .orElse("");
    }

    public static String formatTimeSimple(final LocalTime time) {
        return formatTime(time, FORMAT_HHMM);
    }

    public static ZoneOffset getZoneOffsetFromDateTimeAtZone(final LocalDateTime timestamp, final String zone) {
        return ZoneId.of(zone).getRules().getOffset(timestamp);
    }

    public static ZoneOffset getZoneOffsetAtZone(final Optional<String> zone) {
        final ZoneId zoneId = ZoneId.of(zone.orElse(SupportedTimezone.UTC.getTimezoneName()));
        final ZonedDateTime refTimestamp = ZonedDateTime.now(zoneId);

        return zoneId.getRules().getOffset(refTimestamp.toInstant());
    }

    private boolean isTimeMachineMocked() {
        return timehammerConfig.getMocks().isTimeMachineServiceMocked();
    }

    private LocalDateTime now() {
        return LocalDateTime.now(SupportedTimezone.UTC.getZoneId());
    }
}