package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.comunytek.constants.ComunytekSimpleResult;

import java.time.LocalDate;

public class ComunytekHolidayResponseBuilder extends ComunytekResponseBuilder<ComunytekSimpleResult, ComunytekHolidayResponseBuilder> {
    private Boolean holiday;
    private LocalDate date;

    public ComunytekHolidayResponseBuilder holiday(final boolean holiday) {
        this.holiday = holiday;
        return this;
    }

    public ComunytekHolidayResponseBuilder date(final LocalDate date) {
        this.date = date;
        return this;
    }

    public ComunytekHolidayResponse build() {
        final ComunytekHolidayResponse chr = new ComunytekHolidayResponse(result, errorMessage);
        chr.setHoliday(holiday);
        chr.setDate(date);

        return chr;
    }
}