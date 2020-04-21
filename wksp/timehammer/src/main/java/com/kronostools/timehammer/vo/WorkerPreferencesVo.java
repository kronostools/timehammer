package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.service.TimeMachineService;

import java.time.*;

public class WorkerPreferencesVo {
    private final String workerExternalId;
    private final String workSsid;
    private final LocalTime workStartMon;
    private final LocalTime workEndMon;
    private final LocalTime lunchStartMon;
    private final LocalTime lunchEndMon;
    private final LocalTime workStartTue;
    private final LocalTime workEndTue;
    private final LocalTime lunchStartTue;
    private final LocalTime lunchEndTue;
    private final LocalTime workStartWed;
    private final LocalTime workEndWed;
    private final LocalTime lunchStartWed;
    private final LocalTime lunchEndWed;
    private final LocalTime workStartThu;
    private final LocalTime workEndThu;
    private final LocalTime lunchStartThu;
    private final LocalTime lunchEndThu;
    private final LocalTime workStartFri;
    private final LocalTime workEndFri;
    private final LocalTime lunchStartFri;
    private final LocalTime lunchEndFri;
    private final String cityCode;
    private final String cityTimezone;

    public WorkerPreferencesVo(final String workerExternalId, final String workSsid,
                               final LocalTime workStartMon, final LocalTime workEndMon, final LocalTime lunchStartMon, final LocalTime lunchEndMon,
                               final LocalTime workStartTue, final LocalTime workEndTue, final LocalTime lunchStartTue, final LocalTime lunchEndTue,
                               final LocalTime workStartWed, final LocalTime workEndWed, final LocalTime lunchStartWed, final LocalTime lunchEndWed,
                               final LocalTime workStartThu, final LocalTime workEndThu, final LocalTime lunchStartThu, final LocalTime lunchEndThu,
                               final LocalTime workStartFri, final LocalTime workEndFri, final LocalTime lunchStartFri, final LocalTime lunchEndFri,
                               final String cityCode) {
        this(workerExternalId, workSsid,
                workStartMon, workEndMon, lunchStartMon, lunchEndMon,
                workStartTue, workEndTue, lunchStartTue, lunchEndTue,
                workStartWed, workEndWed, lunchStartWed, lunchEndWed,
                workStartThu, workEndThu, lunchStartThu, lunchEndThu,
                workStartFri, workEndFri, lunchStartFri, lunchEndFri,
                cityCode, null);
    }

    public WorkerPreferencesVo(final String workerExternalId, final String workSsid,
                               final LocalTime workStartMon, final LocalTime workEndMon, final LocalTime lunchStartMon, final LocalTime lunchEndMon,
                               final LocalTime workStartTue, final LocalTime workEndTue, final LocalTime lunchStartTue, final LocalTime lunchEndTue,
                               final LocalTime workStartWed, final LocalTime workEndWed, final LocalTime lunchStartWed, final LocalTime lunchEndWed,
                               final LocalTime workStartThu, final LocalTime workEndThu, final LocalTime lunchStartThu, final LocalTime lunchEndThu,
                               final LocalTime workStartFri, final LocalTime workEndFri, final LocalTime lunchStartFri, final LocalTime lunchEndFri,
                               final String cityCode, final String cityTimezone) {
        this.workerExternalId = workerExternalId;
        this.workSsid = workSsid;
        this.workStartMon = workStartMon;
        this.workEndMon = workEndMon;
        this.lunchStartMon = lunchStartMon;
        this.lunchEndMon = lunchEndMon;
        this.workStartTue = workStartTue;
        this.workEndTue = workEndTue;
        this.lunchStartTue = lunchStartTue;
        this.lunchEndTue = lunchEndTue;
        this.workStartWed = workStartWed;
        this.workEndWed = workEndWed;
        this.lunchStartWed = lunchStartWed;
        this.lunchEndWed = lunchEndWed;
        this.workStartThu = workStartThu;
        this.workEndThu = workEndThu;
        this.lunchStartThu = lunchStartThu;
        this.lunchEndThu = lunchEndThu;
        this.workStartFri = workStartFri;
        this.workEndFri = workEndFri;
        this.lunchStartFri = lunchStartFri;
        this.lunchEndFri = lunchEndFri;
        this.cityCode = cityCode;
        this.cityTimezone = cityTimezone;
    }

    public String getWorkerExternalId() {
        return workerExternalId;
    }

    public String getWorkSsid() {
        return workSsid;
    }

    public LocalTime getWorkStartMon() {
        return workStartMon;
    }

    public LocalTime getWorkEndMon() {
        return workEndMon;
    }

    public LocalTime getLunchStartMon() {
        return lunchStartMon;
    }

    public LocalTime getLunchEndMon() {
        return lunchEndMon;
    }

    public LocalTime getWorkStartTue() {
        return workStartTue;
    }

    public LocalTime getWorkEndTue() {
        return workEndTue;
    }

    public LocalTime getLunchStartTue() {
        return lunchStartTue;
    }

    public LocalTime getLunchEndTue() {
        return lunchEndTue;
    }

    public LocalTime getWorkStartWed() {
        return workStartWed;
    }

    public LocalTime getWorkEndWed() {
        return workEndWed;
    }

    public LocalTime getLunchStartWed() {
        return lunchStartWed;
    }

    public LocalTime getLunchEndWed() {
        return lunchEndWed;
    }

    public LocalTime getWorkStartThu() {
        return workStartThu;
    }

    public LocalTime getWorkEndThu() {
        return workEndThu;
    }

    public LocalTime getLunchStartThu() {
        return lunchStartThu;
    }

    public LocalTime getLunchEndThu() {
        return lunchEndThu;
    }

    public LocalTime getWorkStartFri() {
        return workStartFri;
    }

    public LocalTime getWorkEndFri() {
        return workEndFri;
    }

    public LocalTime getLunchStartFri() {
        return lunchStartFri;
    }

    public LocalTime getLunchEndFri() {
        return lunchEndFri;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getCityTimezone() {
        return cityTimezone;
    }

    public WorkerCurrentPreferencesVo getAt(final LocalDateTime timestamp) {
        final DayOfWeek dayOfWeek = timestamp.getDayOfWeek();

        LocalTime workStart = null;
        LocalTime workEnd = null;
        LocalTime lunchStart = null;
        LocalTime lunchEnd = null;

        switch (dayOfWeek) {
            case MONDAY:
                workStart = workStartMon;
                workEnd = workEndMon;
                lunchStart = lunchStartMon;
                lunchEnd = lunchEndMon;
                break;
            case TUESDAY:
                workStart = workStartTue;
                workEnd = workEndTue;
                lunchStart = lunchStartTue;
                lunchEnd = lunchEndTue;
                break;
            case WEDNESDAY:
                workStart = workStartWed;
                workEnd = workEndWed;
                lunchStart = lunchStartWed;
                lunchEnd = lunchEndWed;
                break;
            case THURSDAY:
                workStart = workStartThu;
                workEnd = workEndThu;
                lunchStart = lunchStartThu;
                lunchEnd = lunchEndThu;
                break;
            case FRIDAY:
                workStart = workStartFri;
                workEnd = workEndFri;
                lunchStart = lunchStartFri;
                lunchEnd = lunchEndFri;
                break;
        }

        final ZoneOffset zoneOffSet = ZoneId.of(cityTimezone).getRules().getOffset(timestamp);

        return new WorkerCurrentPreferencesVo(timestamp, workerExternalId, workSsid,
                TimeMachineService.getTimeWithOffset(workStart, zoneOffSet),
                TimeMachineService.getTimeWithOffset(workEnd, zoneOffSet),
                TimeMachineService.getTimeWithOffset(lunchStart, zoneOffSet),
                TimeMachineService.getTimeWithOffset(lunchEnd, zoneOffSet),
                cityCode, cityTimezone);
    }

    @Override
    public String toString() {
        return "WorkerPreferencesVo{" +
                "workerExternalId='" + workerExternalId + '\'' +
                ", workSsid='" + workSsid + '\'' +
                ", workStartMon=" + TimeMachineService.formatTimeSimple(workStartMon) +
                ", workEndMon=" + TimeMachineService.formatTimeSimple(workEndMon) +
                ", lunchStartMon=" + TimeMachineService.formatTimeSimple(lunchStartMon) +
                ", lunchEndMon=" + TimeMachineService.formatTimeSimple(lunchEndMon) +
                ", workStartTue=" + TimeMachineService.formatTimeSimple(workStartTue) +
                ", workEndTue=" + TimeMachineService.formatTimeSimple(workEndTue) +
                ", lunchStartTue=" + TimeMachineService.formatTimeSimple(lunchStartTue) +
                ", lunchEndTue=" + TimeMachineService.formatTimeSimple(lunchEndTue) +
                ", workStartWed=" + TimeMachineService.formatTimeSimple(workStartWed) +
                ", workEndWed=" + TimeMachineService.formatTimeSimple(workEndWed) +
                ", lunchStartWed=" + TimeMachineService.formatTimeSimple(lunchStartWed) +
                ", lunchEndWed=" + TimeMachineService.formatTimeSimple(lunchEndWed) +
                ", workStartThu=" + TimeMachineService.formatTimeSimple(workStartThu) +
                ", workEndThu=" + TimeMachineService.formatTimeSimple(workEndThu) +
                ", lunchStartThu=" + TimeMachineService.formatTimeSimple(lunchStartThu) +
                ", lunchEndThu=" + TimeMachineService.formatTimeSimple(lunchEndThu) +
                ", workStartFri=" + TimeMachineService.formatTimeSimple(workStartFri) +
                ", workEndFri=" + TimeMachineService.formatTimeSimple(workEndFri) +
                ", lunchStartFri=" + TimeMachineService.formatTimeSimple(lunchStartFri) +
                ", lunchEndFri=" + TimeMachineService.formatTimeSimple(lunchEndFri) +
                ", cityCode='" + cityCode + '\'' +
                ", cityTimezone='" + cityTimezone + '\'' +
                '}';
    }
}