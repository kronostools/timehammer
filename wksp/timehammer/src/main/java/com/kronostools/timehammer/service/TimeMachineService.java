package com.kronostools.timehammer.service;

import com.kronostools.timehammer.config.TimehammerConfig;
import com.kronostools.timehammer.enums.SupportedTimezone;

import javax.enterprise.context.ApplicationScoped;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public static final Locale LOCALE_ES_ES = Locale.forLanguageTag("es-ES");

    private LocalDateTime now;

    private final TimehammerConfig timehammerConfig;

    public TimeMachineService(final TimehammerConfig timehammerConfig) {
        this.now = now();
        this.timehammerConfig = timehammerConfig;
    }

    public void timeTravelToDateTimeWithZone(final LocalDateTime newTimestamp, final SupportedTimezone zone) {
        now = newTimestamp.atOffset(zone.getOffset(newTimestamp)).withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    public LocalDateTime getNow() {
        if (isTimeMachineMocked()) {
            return now;
        } else {
            return now();
        }
    }

    public LocalDateTime getNowAtZone(final SupportedTimezone zone) {
        LocalDateTime now = getNow();

        return now.atOffset(ZoneOffset.UTC).withOffsetSameInstant(zone.getOffset(now)).toLocalDateTime();
    }

    public LocalDateTime getTodayAtMidnight() {
        return getNow().withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    public LocalDate[] getAllYearBusinessDays() {
        final LocalDate firstDayOfYear = this.getNow().toLocalDate().withDayOfYear(1);

        return IntStream.range(1, 367)
                .mapToObj(firstDayOfYear::withDayOfYear)
                .filter(TimeMachineService::isBusinessDay)
                .collect(Collectors.toList())
                .toArray(LocalDate[]::new);
    }

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

    private boolean isTimeMachineMocked() {
        return timehammerConfig.getMocks().isTimeMachineServiceMocked();
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

    private LocalDateTime now() {
        return LocalDateTime.now(SupportedTimezone.UTC.getZone());
    }
}