package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.comunytek.constants.ComunytekSimpleResult;

import java.time.LocalDate;

public class ComunytekHolidayResponse extends ComunytekResponse<ComunytekSimpleResult> {
    private boolean holiday;
    private LocalDate date;

    ComunytekHolidayResponse(final ComunytekSimpleResult simpleResult, final String errorMessage) {
        super(simpleResult, errorMessage);
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
