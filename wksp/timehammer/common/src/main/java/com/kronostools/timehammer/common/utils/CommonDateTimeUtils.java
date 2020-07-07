package com.kronostools.timehammer.common.utils;

import com.kronostools.timehammer.common.constants.SupportedTimezone;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

public final class CommonDateTimeUtils {
    private static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    private static final String FORMAT_YYYYMMDDTHHMM = "yyyyMMdd'T'HH:mm";
    private static final String FORMAT_YYYYMMDDTHHMMSSSSS = "yyyyMMdd'T'HH:mm:ss.SSS";
    private static final String FORMAT_DDMMYYYY_DSEP_FWS = "dd/MM/yyyy";
    private static final String FORMAT_HHMM_TSEP = "HH:mm";
    private static final String FORMAT_HHMMSSSSS_TSEP = "HH:mm:ss.SSS";

    public static final DateTimeFormatter FORMATTER_HHMM_TSEP = DateTimeFormatter.ofPattern(FORMAT_HHMM_TSEP);

    public static final Locale LOCALE_ES_ES = Locale.forLanguageTag("es-ES");

    private CommonDateTimeUtils() {}

    public static LocalDateTime parseDateTimeWithZone(final String[] dateTimeParts, final String[] dateTimeFormat, final SupportedTimezone zone) {
        final LocalDate dateAtTimezone = LocalDate.parse(dateTimeParts[0], DateTimeFormatter.ofPattern(dateTimeFormat[0]));
        final LocalTime timeAtTimezone = LocalTime.parse(dateTimeParts[1], DateTimeFormatter.ofPattern(dateTimeFormat[1]));

        return ZonedDateTime
                .of(dateAtTimezone, timeAtTimezone, zone.getZone())
                .withZoneSameInstant(SupportedTimezone.UTC.getZone())
                .toLocalDateTime();
    }

    public static LocalDate parseDateFromComunytek(final String date) {
        return parseDate(date, FORMAT_DDMMYYYY_DSEP_FWS);
    }

    public static LocalDateTime parseDateTimeFromJson(final String dateTime) {
        return parseDateTime(dateTime, FORMAT_YYYYMMDDTHHMMSSSSS);
    }

    public static LocalTime parseTimeFromConfig(final String time) {
        return parseTime(time, FORMAT_HHMM_TSEP);
    }

    public static LocalTime parseTimeFromForm(final String time) {
        return parseTime(time, FORMAT_HHMM_TSEP);
    }

    public static LocalTime parseTimeFromComunytek(final String time) {
        return parseTime(time, FORMAT_HHMMSSSSS_TSEP);
    }

    public static LocalTime getTimeWithOffset(final LocalTime time, final ZoneOffset offset) {
        return Optional.ofNullable(time)
                .map(t -> t.atOffset(offset)
                        .withOffsetSameInstant(ZoneOffset.UTC)
                        .toLocalTime())
                .orElse(null);
    }

    public static LocalDateTime getDateTimeWithZone(final LocalDateTime dateTime, final SupportedTimezone zone) {
        return Optional.ofNullable(dateTime)
                .map(dt -> dt.atOffset(zone.getOffset(dt))
                        .withOffsetSameInstant(ZoneOffset.UTC)
                        .toLocalDateTime())
                .orElse(null);
    }

    public static LocalDateTime getDateTimeAtZone(final LocalDateTime dateTime, final SupportedTimezone zone) {
        return Optional.ofNullable(dateTime)
                .map(dt -> dt.atOffset(ZoneOffset.UTC)
                            .withOffsetSameInstant(zone.getOffset(dt))
                            .toLocalDateTime())
                .orElse(null);
    }

    public static LocalDateTime getDateTimeAtZone(final LocalDate date, final LocalTime time, final SupportedTimezone zone) {
        return Optional.ofNullable(LocalDateTime.of(date, time))
                .map(dt -> dt.atOffset(ZoneOffset.UTC)
                        .withOffsetSameInstant(zone.getOffset(dt))
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

    public static String formatDateTimeToLog(final LocalDateTime dateTime) {
        return formatDateTime(dateTime, FORMAT_YYYYMMDDTHHMMSSSSS);
    }

    public static String formatDateTimeToJson(final LocalDateTime dateTime) {
        return formatDateTime(dateTime, FORMAT_YYYYMMDDTHHMMSSSSS);
    }

    public static String formatDateTimeToComunytek(final LocalDateTime dateTime) {
        return formatDateTime(dateTime, FORMAT_YYYYMMDD);
    }

    public static String formatTimezoneToLog(final SupportedTimezone zone) {
        return zone.getTimezoneName();
    }

    public static String formatTimezoneToJson(final SupportedTimezone zone) {
        return zone.getTimezoneName();
    }

    public static String formatDateToLog(final LocalDate date) {
        return formatDate(date, FORMAT_YYYYMMDD);
    }

    public static String formatDateToChatbot(final LocalDate date) {
        return formatDate(date, FORMAT_DDMMYYYY_DSEP_FWS);
    }

    public static String formatTimeSimple(final LocalTime time) {
        return formatTime(time, FORMAT_HHMM_TSEP);
    }

    public static String formatTimeToChatbot(final LocalTime time) {
        return formatTime(time, FORMAT_HHMM_TSEP);
    }

    public static String formatDayOfWeekToChatbot(final DayOfWeek dow) {
        String day = null;

        switch (dow) {
            case MONDAY:
                day = "Lunes";
                break;
            case TUESDAY:
                day = "Martes";
                break;
            case WEDNESDAY:
                day = "Miércoles";
                break;
            case THURSDAY:
                day = "Jueves";
                break;
            case FRIDAY:
                day = "Viernes";
                break;
            case SATURDAY:
                day = "Sábado";
                break;
            case SUNDAY:
                day = "Domingo";
                break;
        }

        return day;
    }

    public static boolean isTimeFromFormValid(final String time) {
        return isTimeValid(time, FORMATTER_HHMM_TSEP);
    }

    public static boolean isTimeIntervalFromFormValid(final String start, final String end) {
        return isTimeIntervalValid(start, end, FORMATTER_HHMM_TSEP);
    }

    public static boolean isTimeIntervalFromFormWithin(final String innerStart, final String innerEnd, final String outerStart, final String outerEnd) {
        return isValidTimeIntervalWithin(innerStart, innerEnd, outerStart, outerEnd, FORMATTER_HHMM_TSEP);
    }

    public static boolean isWeekend(final LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    public static boolean isBusinessDay(final LocalDate date) {
        return !isWeekend(date);
    }

    public static String getDayOfWeekLocalizedName(final LocalDateTime dateTime, final Locale locale) {
        return dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, locale);
    }

    public static LocalDateTime atMidday(final LocalDate date) {
        return date.atTime(12, 0);
    }

    public static LocalDateTime atMidnight(final LocalDate date) {
        return date.atTime(23, 59);
    }

    private static String formatDateTime(final LocalDateTime dateTime, final String format) {
        return Optional.ofNullable(dateTime)
                .map(dt -> dt.format(DateTimeFormatter.ofPattern(format)))
                .orElse("");
    }

    private static String formatDate(final LocalDate date, final String dateFormat) {
        return Optional.ofNullable(date)
                .map(d -> d.format(DateTimeFormatter.ofPattern(dateFormat)))
                .orElse("");
    }

    private static String formatTime(final LocalTime time, final String timeFormat) {
        return Optional.ofNullable(time)
                .map(t -> t.format(DateTimeFormatter.ofPattern(timeFormat)))
                .orElse("");
    }

    private static LocalTime parseTime(final String time, final String timeFormat) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern(timeFormat));
    }

    private static LocalDate parseDate(final String date, String dateFormat) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat));
    }

    private static LocalDateTime parseDateTime(final String dateTime, final String dateTimeFormat) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(dateTimeFormat));
    }

    private static boolean isTimeValid(final String time, final DateTimeFormatter timeFormat) {
        try {
            LocalTime.parse(time, timeFormat);
        } catch (DateTimeParseException e) {
            return false;
        }

        return true;
    }

    private static boolean isTimeIntervalValid(String start, String end, DateTimeFormatter formatter) {
        try {
            final LocalTime startTime = LocalTime.parse(start, FORMATTER_HHMM_TSEP);
            final LocalTime endTime = LocalTime.parse(end, FORMATTER_HHMM_TSEP);

            return startTime.isBefore(endTime);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static boolean isValidTimeIntervalWithin(String innerStart, String innerEnd, String outerStart, String outerEnd, DateTimeFormatter formatter) {
        try {
            final LocalTime innerStartTime = LocalTime.parse(innerStart, formatter);
            final LocalTime innerEndTime = LocalTime.parse(innerEnd, formatter);
            final LocalTime outerStartTime = LocalTime.parse(outerStart, formatter);
            final LocalTime outerEndTime = LocalTime.parse(outerEnd, formatter);

            return outerStartTime.isBefore(innerStartTime) && outerEndTime.isAfter(innerEndTime);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}