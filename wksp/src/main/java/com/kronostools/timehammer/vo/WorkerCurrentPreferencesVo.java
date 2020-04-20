package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.service.TimeMachineService;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class WorkerCurrentPreferencesVo {
    private final LocalDateTime timestamp;
    private final String workerExternalId;
    private final String workSsid;
    private final LocalTime workStart;
    private final LocalTime workEnd;
    private final LocalTime lunchStart;
    private final LocalTime lunchEnd;
    private final String cityCode;
    private final String cityTimezone;

    public WorkerCurrentPreferencesVo(final LocalDateTime timestamp, final String workerExternalId, final String workSsid,
                                      final LocalTime workStart, final LocalTime workEnd, final LocalTime lunchStart, final LocalTime lunchEnd,
                                      final String cityCode, final String cityTimezone) {
        this.timestamp = timestamp;
        this.workerExternalId = workerExternalId;
        this.workSsid = workSsid;
        this.workStart = workStart;
        this.workEnd = workEnd;
        this.lunchStart = lunchStart;
        this.lunchEnd = lunchEnd;
        this.cityCode = cityCode;
        this.cityTimezone = cityTimezone;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getWorkerExternalId() {
        return workerExternalId;
    }

    public String getWorkSsid() {
        return workSsid;
    }

    public LocalTime getWorkStart() {
        return workStart;
    }

    public LocalTime getWorkEnd() {
        return workEnd;
    }

    public LocalTime getLunchStart() {
        return lunchStart;
    }

    public LocalTime getLunchEnd() {
        return lunchEnd;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getCityTimezone() {
        return cityTimezone;
    }

    public Boolean workToday() {
        return workStart != null;
    }

    public Boolean isTimeToStartWorking() {
        return workToday()
                && timestamp.toLocalTime().isAfter(workStart);
    }

    public Boolean isTimeToEndWorking() {
        return workToday()
                && timestamp.toLocalTime().isAfter(workEnd);
    }

    public Boolean lunchToday() {
        return lunchStart != null;
    }

    public Boolean isTimeToStartLunch() {
        return lunchToday()
                && timestamp.toLocalTime().isAfter(lunchStart);
    }

    public Boolean isTimeToEndLunch() {
        return lunchToday()
                && timestamp.toLocalTime().isAfter(lunchEnd);
    }

    @Override
    public String toString() {
        return "WorkerCurrentPreferencesVo{" +
                "timestamp='" + TimeMachineService.formatDateTimeFull(timestamp) + '\'' +
                ", workerExternalId='" + workerExternalId + '\'' +
                ", workSsid='" + workSsid + '\'' +
                ", workStart=" + TimeMachineService.formatTimeSimple(workStart) +
                ", workEnd=" + TimeMachineService.formatTimeSimple(workEnd) +
                ", lunchStart=" + TimeMachineService.formatTimeSimple(lunchStart) +
                ", lunchEnd=" + TimeMachineService.formatTimeSimple(lunchEnd) +
                ", cityCode='" + cityCode + '\'' +
                ", cityTimezone='" + cityTimezone + '\'' +
                '}';
    }
}