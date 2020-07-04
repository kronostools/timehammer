package com.kronostools.timehammer.comunytek.model;

import com.kronostools.timehammer.common.messages.constants.WorkerStatusContext;
import com.kronostools.timehammer.common.messages.constants.WorkerStatusContextConverter;
import com.kronostools.timehammer.comunytek.constants.ComunytekStatusResult;
import com.kronostools.timehammer.comunytek.constants.ComunytekStatusValue;

import java.time.LocalDate;
import java.time.LocalTime;

public class ComunytekStatusResponse extends ComunytekResponse<ComunytekStatusResult> implements WorkerStatusContextConverter {
    private LocalDate date;
    private LocalTime time;
    private ComunytekStatusValue status;
    private String workedTime;
    private String comment;

    ComunytekStatusResponse(final ComunytekStatusResult simpleResult, final String errorMessage) {
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

    @Override
    public WorkerStatusContext toWorkerStatusContext() {
        final WorkerStatusContext statusContext;

        switch (status) {
            case INITIAL:
                statusContext = WorkerStatusContext.BEFORE_WORK;
                break;
            case STARTED:
                statusContext = WorkerStatusContext.WORK_BEFORE_LUNCH;
                break;
            case RESUMED:
                statusContext = WorkerStatusContext.WORK_AFTER_LUNCH;
                break;
            case PAUSED:
                if (isLunch()) {
                    statusContext = WorkerStatusContext.LUNCH;
                } else {
                    statusContext = WorkerStatusContext.UNKNOWN;
                }
                break;
            case ENDED:
                statusContext = WorkerStatusContext.AFTER_WORK;
                break;
            default:
                statusContext = WorkerStatusContext.UNKNOWN;
        }

        return statusContext;
    }

    private boolean isLunch() {
        return comment != null && comment.toLowerCase().contains("comida");
    }
}