package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.service.TimeMachineService;

import java.time.LocalDate;

public class HolidayVo {
    private final LocalDate day;

    public HolidayVo(final LocalDate day) {
        this.day = day;
    }

    public LocalDate getDay() {
        return day;
    }

    @Override
    public String toString() {
        return "HolidayVo{" +
                "holiday=" + TimeMachineService.formatDate(day) +
                '}';
    }
}