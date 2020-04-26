package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.enums.SupportedTimezone;
import com.kronostools.timehammer.service.TimeMachineService;

import java.time.LocalTime;

public class WorkerPreferencesVo {
    private final String workerExternalId;
    private final String workSsid;
    private final LocalTime zonedWorkStartMon;
    private final LocalTime zonedWorkEndMon;
    private final LocalTime zonedLunchStartMon;
    private final LocalTime zonedLunchEndMon;
    private final LocalTime zonedWorkStartTue;
    private final LocalTime zonedWorkEndTue;
    private final LocalTime zonedLunchStartTue;
    private final LocalTime zonedLunchEndTue;
    private final LocalTime zonedWorkStartWed;
    private final LocalTime zonedWorkEndWed;
    private final LocalTime zonedLunchStartWed;
    private final LocalTime zonedLunchEndWed;
    private final LocalTime zonedWorkStartThu;
    private final LocalTime zonedWorkEndThu;
    private final LocalTime zonedLunchStartThu;
    private final LocalTime zonedLunchEndThu;
    private final LocalTime zonedWorkStartFri;
    private final LocalTime zonedWorkEndFri;
    private final LocalTime zonedLunchStartFri;
    private final LocalTime zonedLunchEndFri;
    private final String cityCode;
    private final SupportedTimezone timezone;
    private final String companyCode;

    public WorkerPreferencesVo(final String workerExternalId, final String workSsid,
                               final LocalTime zonedWorkStartMon, final LocalTime zonedWorkEndMon, final LocalTime zonedLunchStartMon, final LocalTime zonedLunchEndMon,
                               final LocalTime zonedWorkStartTue, final LocalTime zonedWorkEndTue, final LocalTime zonedLunchStartTue, final LocalTime zonedLunchEndTue,
                               final LocalTime zonedWorkStartWed, final LocalTime zonedWorkEndWed, final LocalTime zonedLunchStartWed, final LocalTime zonedLunchEndWed,
                               final LocalTime zonedWorkStartThu, final LocalTime zonedWorkEndThu, final LocalTime zonedLunchStartThu, final LocalTime zonedLunchEndThu,
                               final LocalTime zonedWorkStartFri, final LocalTime zonedWorkEndFri, final LocalTime zonedLunchStartFri, final LocalTime zonedLunchEndFri,
                               final String cityCode, final String companyCode) {
        this(workerExternalId, workSsid,
                zonedWorkStartMon, zonedWorkEndMon, zonedLunchStartMon, zonedLunchEndMon,
                zonedWorkStartTue, zonedWorkEndTue, zonedLunchStartTue, zonedLunchEndTue,
                zonedWorkStartWed, zonedWorkEndWed, zonedLunchStartWed, zonedLunchEndWed,
                zonedWorkStartThu, zonedWorkEndThu, zonedLunchStartThu, zonedLunchEndThu,
                zonedWorkStartFri, zonedWorkEndFri, zonedLunchStartFri, zonedLunchEndFri,
                cityCode, null, companyCode);
    }

    public WorkerPreferencesVo(final String workerExternalId, final String workSsid,
                               final LocalTime zonedWorkStartMon, final LocalTime zonedWorkEndMon, final LocalTime zonedLunchStartMon, final LocalTime zonedLunchEndMon,
                               final LocalTime zonedWorkStartTue, final LocalTime zonedWorkEndTue, final LocalTime zonedLunchStartTue, final LocalTime zonedLunchEndTue,
                               final LocalTime zonedWorkStartWed, final LocalTime zonedWorkEndWed, final LocalTime zonedLunchStartWed, final LocalTime zonedLunchEndWed,
                               final LocalTime zonedWorkStartThu, final LocalTime zonedWorkEndThu, final LocalTime zonedLunchStartThu, final LocalTime zonedLunchEndThu,
                               final LocalTime zonedWorkStartFri, final LocalTime zonedWorkEndFri, final LocalTime zonedLunchStartFri, final LocalTime zonedLunchEndFri,
                               final String cityCode, final SupportedTimezone timezone, final String companyCode) {
        this.workerExternalId = workerExternalId;
        this.workSsid = workSsid;
        this.zonedWorkStartMon = zonedWorkStartMon;
        this.zonedWorkEndMon = zonedWorkEndMon;
        this.zonedLunchStartMon = zonedLunchStartMon;
        this.zonedLunchEndMon = zonedLunchEndMon;
        this.zonedWorkStartTue = zonedWorkStartTue;
        this.zonedWorkEndTue = zonedWorkEndTue;
        this.zonedLunchStartTue = zonedLunchStartTue;
        this.zonedLunchEndTue = zonedLunchEndTue;
        this.zonedWorkStartWed = zonedWorkStartWed;
        this.zonedWorkEndWed = zonedWorkEndWed;
        this.zonedLunchStartWed = zonedLunchStartWed;
        this.zonedLunchEndWed = zonedLunchEndWed;
        this.zonedWorkStartThu = zonedWorkStartThu;
        this.zonedWorkEndThu = zonedWorkEndThu;
        this.zonedLunchStartThu = zonedLunchStartThu;
        this.zonedLunchEndThu = zonedLunchEndThu;
        this.zonedWorkStartFri = zonedWorkStartFri;
        this.zonedWorkEndFri = zonedWorkEndFri;
        this.zonedLunchStartFri = zonedLunchStartFri;
        this.zonedLunchEndFri = zonedLunchEndFri;
        this.cityCode = cityCode;
        this.timezone = timezone;
        this.companyCode = companyCode;
    }

    public String getWorkerExternalId() {
        return workerExternalId;
    }

    public String getWorkSsid() {
        return workSsid;
    }

    public LocalTime getZonedWorkStartMon() {
        return zonedWorkStartMon;
    }

    public LocalTime getZonedWorkEndMon() {
        return zonedWorkEndMon;
    }

    public LocalTime getZonedLunchStartMon() {
        return zonedLunchStartMon;
    }

    public LocalTime getZonedLunchEndMon() {
        return zonedLunchEndMon;
    }

    public LocalTime getZonedWorkStartTue() {
        return zonedWorkStartTue;
    }

    public LocalTime getZonedWorkEndTue() {
        return zonedWorkEndTue;
    }

    public LocalTime getZonedLunchStartTue() {
        return zonedLunchStartTue;
    }

    public LocalTime getZonedLunchEndTue() {
        return zonedLunchEndTue;
    }

    public LocalTime getZonedWorkStartWed() {
        return zonedWorkStartWed;
    }

    public LocalTime getZonedWorkEndWed() {
        return zonedWorkEndWed;
    }

    public LocalTime getZonedLunchStartWed() {
        return zonedLunchStartWed;
    }

    public LocalTime getZonedLunchEndWed() {
        return zonedLunchEndWed;
    }

    public LocalTime getZonedWorkStartThu() {
        return zonedWorkStartThu;
    }

    public LocalTime getZonedWorkEndThu() {
        return zonedWorkEndThu;
    }

    public LocalTime getZonedLunchStartThu() {
        return zonedLunchStartThu;
    }

    public LocalTime getZonedLunchEndThu() {
        return zonedLunchEndThu;
    }

    public LocalTime getZonedWorkStartFri() {
        return zonedWorkStartFri;
    }

    public LocalTime getZonedWorkEndFri() {
        return zonedWorkEndFri;
    }

    public LocalTime getZonedLunchStartFri() {
        return zonedLunchStartFri;
    }

    public LocalTime getZonedLunchEndFri() {
        return zonedLunchEndFri;
    }

    public String getCityCode() {
        return cityCode;
    }

    public SupportedTimezone getTimezone() {
        return timezone;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    @Override
    public String toString() {
        return "WorkerPreferencesVo{" +
                "workerExternalId='" + workerExternalId + '\'' +
                ", workSsid='" + workSsid + '\'' +
                ", zonedWorkStartMon=" + TimeMachineService.formatTimeSimple(zonedWorkStartMon) +
                ", zonedWorkEndMon=" + TimeMachineService.formatTimeSimple(zonedWorkEndMon) +
                ", zonedLunchStartMon=" + TimeMachineService.formatTimeSimple(zonedLunchStartMon) +
                ", zonedLunchEndMon=" + TimeMachineService.formatTimeSimple(zonedLunchEndMon) +
                ", zonedWorkStartTue=" + TimeMachineService.formatTimeSimple(zonedWorkStartTue) +
                ", zonedWorkEndTue=" + TimeMachineService.formatTimeSimple(zonedWorkEndTue) +
                ", zonedLunchStartTue=" + TimeMachineService.formatTimeSimple(zonedLunchStartTue) +
                ", zonedLunchEndTue=" + TimeMachineService.formatTimeSimple(zonedLunchEndTue) +
                ", zonedWorkStartWed=" + TimeMachineService.formatTimeSimple(zonedWorkStartWed) +
                ", zonedWorkEndWed=" + TimeMachineService.formatTimeSimple(zonedWorkEndWed) +
                ", zonedLunchStartWed=" + TimeMachineService.formatTimeSimple(zonedLunchStartWed) +
                ", zonedLunchEndWed=" + TimeMachineService.formatTimeSimple(zonedLunchEndWed) +
                ", zonedWorkStartThu=" + TimeMachineService.formatTimeSimple(zonedWorkStartThu) +
                ", zonedWorkEndThu=" + TimeMachineService.formatTimeSimple(zonedWorkEndThu) +
                ", zonedLunchStartThu=" + TimeMachineService.formatTimeSimple(zonedLunchStartThu) +
                ", zonedLunchEndThu=" + TimeMachineService.formatTimeSimple(zonedLunchEndThu) +
                ", zonedWorkStartFri=" + TimeMachineService.formatTimeSimple(zonedWorkStartFri) +
                ", zonedWorkEndFri=" + TimeMachineService.formatTimeSimple(zonedWorkEndFri) +
                ", zonedLunchStartFri=" + TimeMachineService.formatTimeSimple(zonedLunchStartFri) +
                ", zonedLunchEndFri=" + TimeMachineService.formatTimeSimple(zonedLunchEndFri) +
                ", cityCode='" + cityCode + '\'' +
                ", timezone='" + timezone.name() + '\'' +
                ", companyCode='" + companyCode + '\'' +
                '}';
    }
}