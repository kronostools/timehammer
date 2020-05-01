package com.kronostools.timehammer.common.utils;

import com.kronostools.timehammer.common.constants.SupportedTimezone;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public class CommonDateTimeUtils {
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String FORMAT_YYYYMMDDTHHMM = "yyyyMMdd'T'HH:mm";
    public static final String FORMAT_YYYYMMDDTHHMMSSSSS = "yyyyMMdd'T'HH:mm:ss.SSS";
    public static final String FORMAT_DDMMYYYY_DSEP_FWS = "dd/MM/yyyy";
    public static final String FORMAT_HHMM_TSEP = "HH:mm";
    public static final String FORMAT_HHMMSSSSS_TSEP = "HH:mm:ss.SSS";

    public static final DateTimeFormatter FORMATTER_HHMM_TSEP = DateTimeFormatter.ofPattern(FORMAT_HHMM_TSEP);

    public static final Locale LOCALE_ES_ES = Locale.forLanguageTag("es-ES");

    public static LocalDateTime parseDateTimeWithZone(final String[] dateTimeParts, final String[] dateTimeFormat, final SupportedTimezone zone) {
        final LocalDate dateAtTimezone = LocalDate.parse(dateTimeParts[0], DateTimeFormatter.ofPattern(dateTimeFormat[0]));
        final LocalTime timeAtTimezone = LocalTime.parse(dateTimeParts[1], DateTimeFormatter.ofPattern(dateTimeFormat[1]));

        return ZonedDateTime
                .of(dateAtTimezone, timeAtTimezone, zone.getZone())
                .withZoneSameInstant(SupportedTimezone.UTC.getZone())
                .toLocalDateTime();
    }

    public static LocalDate parseDate(final String date, String dateFormat) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat));
    }

    public static LocalDateTime parseDateTime(final String dateTime, final String dateTimeFormat) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(dateTimeFormat));
    }

    public static LocalTime parseTime(final String time, final String timeFormat) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern(timeFormat));
    }

    public static LocalTime parseTimeSimple(final String time) {
        return parseTime(time, FORMAT_HHMM_TSEP);
    }

    public static LocalTime getTimeWithOffset(final LocalTime time, final ZoneOffset offset) {
        return Optional.ofNullable(time)
                .map(t -> t.atOffset(offset)
                        .withOffsetSameInstant(ZoneOffset.UTC)
                        .toLocalTime())
                .orElse(null);
    }

    public static LocalDateTime getDateTimeAtZone(final LocalDateTime dateTime, final SupportedTimezone zone) {
        return Optional.ofNullable(dateTime)
                .map(dt -> dateTime.atOffset(ZoneOffset.UTC)
                            .withOffsetSameInstant(zone.getOffset(dateTime))
                            .toLocalDateTime())
                .orElse(null);
    }

    public static LocalDateTime getDateTimeWithZoneAtStartOfDay(final LocalDateTime dateTime, final SupportedTimezone zone) {
        return Optional.ofNullable(dateTime)
                .map(dt -> dt.toLocalDate().atStartOfDay()
                        .atOffset(zone.getOffset(dateTime))
                        .withOffsetSameInstant(ZoneOffset.UTC)
                        .toLocalDateTime())
                .orElse(null);
    }

    public static LocalDateTime getDateTime(final Instant instant) {
        return instant.atOffset(ZoneOffset.UTC).toLocalDateTime();
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
        return formatTime(time, FORMAT_HHMM_TSEP);
    }

    public static boolean isWeekend(final LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    public static boolean isBusinessDay(final LocalDate date) {
        return !isWeekend(date);
    }

    public static String getDayOfWeekFull(final LocalDateTime dateTime, final Locale locale) {
        return dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, locale);
    }

    public static LocalDate toLocalDate(Date date) {
        final LocalDate localDate;

        if (date instanceof java.sql.Date) {
            localDate = ((java.sql.Date) date).toLocalDate();
        } else {
            localDate = date.toInstant().atZone(SupportedTimezone.UTC.getZone()).toLocalDate();
        }

        return localDate;
    }

    public static LocalDateTime atMidday(final LocalDate date) {
        return date.atTime(12, 0);
    }
}