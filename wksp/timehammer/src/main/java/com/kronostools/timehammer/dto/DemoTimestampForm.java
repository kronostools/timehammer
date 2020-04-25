package com.kronostools.timehammer.dto;

import com.kronostools.timehammer.enums.SupportedTimezone;

import java.time.LocalDateTime;

public class DemoTimestampForm {
    private SupportedTimezone timezone;
    private Integer day;
    private Integer month;
    private Integer year;
    private Integer hours;
    private Integer minutes;

    public SupportedTimezone getTimezone() {
        return timezone;
    }

    public void setTimezone(SupportedTimezone timezone) {
        this.timezone = timezone;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.of(this.year, this.month, this.day, this.hours, this.minutes);
    }

    public static DemoTimestampForm fromLocalDateTime(final LocalDateTime timestamp, final SupportedTimezone timezone) {
        DemoTimestampForm result = new DemoTimestampForm();
        result.setTimezone(timezone);
        result.setDay(timestamp.getDayOfMonth());
        result.setMonth(timestamp.getMonthValue());
        result.setYear(timestamp.getYear());
        result.setHours(timestamp.getHour());
        result.setMinutes(timestamp.getMinute());

        return result;
    }

    @Override
    public String toString() {
        return "DemoTimestampForm{" +
                "timezone='" + timezone.name() + '\'' +
                ", day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                ", hours='" + hours + '\'' +
                ", minutes='" + minutes + '\'' +
                '}';
    }
}