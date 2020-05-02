package com.kronostools.timehammer.common.messages.schedules;

import java.time.LocalDate;
import java.util.List;

public class UpdateWorkersHolidaysWorker {
    private String workerInternalId;
    private String workerExternalId;
    private Boolean canBeNotified;
    private String externalPassword;
    private List<LocalDate> holidays;

    public UpdateWorkersHolidaysWorker() {}

    public UpdateWorkersHolidaysWorker(final String workerInternalId, final String workerExternalId, final Boolean canBeNotified) {
        this.workerInternalId = workerInternalId;
        this.workerExternalId = workerExternalId;
        this.canBeNotified = canBeNotified;
    }

    public String getWorkerInternalId() {
        return workerInternalId;
    }

    public void setWorkerInternalId(String workerInternalId) {
        this.workerInternalId = workerInternalId;
    }

    public String getWorkerExternalId() {
        return workerExternalId;
    }

    public void setWorkerExternalId(String workerExternalId) {
        this.workerExternalId = workerExternalId;
    }

    public Boolean getCanBeNotified() {
        return canBeNotified;
    }

    public void setCanBeNotified(Boolean canBeNotified) {
        this.canBeNotified = canBeNotified;
    }

    public String getExternalPassword() {
        return externalPassword;
    }

    public void setExternalPassword(String externalPassword) {
        this.externalPassword = externalPassword;
    }

    public List<LocalDate> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<LocalDate> holidays) {
        this.holidays = holidays;
    }
}