package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.comunytek.constants.ComunytekSimpleResult;

import java.time.LocalDate;

public class ComunytekHolidayResponse extends ComunytekResponse<ComunytekSimpleResult> {
    private Boolean holiday;
    private LocalDate date;

    ComunytekHolidayResponse(final ComunytekSimpleResult simpleResult, final String errorMessage) {
        super(simpleResult, errorMessage);
    }

    public Boolean getHoliday() {
        return holiday;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
