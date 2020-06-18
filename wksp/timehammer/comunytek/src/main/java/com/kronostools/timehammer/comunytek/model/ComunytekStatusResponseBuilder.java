package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.comunytek.constants.ComunytekStatusResult;
import com.kronostools.timehammer.comunytek.constants.ComunytekStatusValue;

import java.time.LocalDate;
import java.time.LocalTime;

public class ComunytekStatusResponseBuilder extends ComunytekResponseBuilder<ComunytekStatusResult, ComunytekStatusResponseBuilder> {
    private LocalDate date;
    private LocalTime time;
    private ComunytekStatusValue status;
    private String workedTime;
    private String comment;

    public ComunytekStatusResponseBuilder date(final LocalDate date) {
        this.date = date;
        return this;
    }

    public ComunytekStatusResponseBuilder time(final LocalTime time) {
        this.time = time;
        return this;
    }

    public ComunytekStatusResponseBuilder status(final ComunytekStatusValue status) {
        this.status = status;
        return this;
    }

    public ComunytekStatusResponseBuilder workedTime(final String workedTime) {
        this.workedTime = workedTime;
        return this;
    }

    public ComunytekStatusResponseBuilder comment(final String comment) {
        this.comment = comment;
        return this;
    }

    public ComunytekStatusResponse build() {
        final ComunytekStatusResponse csr = new ComunytekStatusResponse(result, errorMessage);
        csr.setDate(date);
        csr.setTime(time);
        csr.setStatus(status);
        csr.setWorkedTime(workedTime);
        csr.setComment(comment);

        return csr;
    }
}