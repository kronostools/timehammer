package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.comunytek.constants.ComunytekSimpleResult;
import com.kronostools.timehammer.comunytek.constants.ComunytekStatusValue;

import java.time.LocalDate;
import java.time.LocalTime;

public class ComunytekStatusResponse extends ComunytekResponse<ComunytekSimpleResult> {
    private LocalDate date;
    private LocalTime time;
    private ComunytekStatusValue status;
    private String workedTime;
    private String comment;

    ComunytekStatusResponse(final ComunytekSimpleResult simpleResult, final String errorMessage) {
        super(simpleResult, errorMessage);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public ComunytekStatusValue getStatus() {
        return status;
    }

    public void setStatus(ComunytekStatusValue status) {
        this.status = status;
    }

    public String getWorkedTime() {
        return workedTime;
    }

    public void setWorkedTime(String workedTime) {
        this.workedTime = workedTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}