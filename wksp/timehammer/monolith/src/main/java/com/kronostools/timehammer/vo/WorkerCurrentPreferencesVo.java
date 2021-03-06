package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.enums.Company;
import com.kronostools.timehammer.enums.NonWorkingReason;
import com.kronostools.timehammer.enums.SupportedTimezone;
import com.kronostools.timehammer.service.TimeMachineService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;

public class WorkerCurrentPreferencesVo {
    private final LocalDate date;
    private final String workerInternalId;
    private final String workerExternalId;
    private final String workSsid;
    private final LocalTime zonedWorkStart;
    private final LocalTime zonedWorkEnd;
    private final LocalTime zonedLunchStart;
    private final LocalTime zonedLunchEnd;
    private final LocalTime workStart;
    private final LocalTime workEnd;
    private final LocalTime lunchStart;
    private final LocalTime lunchEnd;
    private final String cityCode;
    private final SupportedTimezone timezone;
    private final Company company;
    private final Boolean workerHoliday;
    private final Boolean cityHoliday;

    public WorkerCurrentPreferencesVo(final LocalDate date, final String workerInternalId, final String workerExternalId, final String workSsid,
                                      final LocalTime zonedWorkStart, final LocalTime zonedWorkEnd, final LocalTime zonedLunchStart, final LocalTime zonedLunchEnd,
                                      final String cityCode, final SupportedTimezone timezone, final Company company,
                                      final Boolean workerHoliday, final Boolean cityHoliday) {
        this.date = date;
        this.workerInternalId = workerInternalId;
        this.workerExternalId = workerExternalId;
        this.workSsid = workSsid;
        this.zonedWorkStart = zonedWorkStart;
        this.zonedWorkEnd = zonedWorkEnd;
        this.zonedLunchStart = zonedLunchStart;
        this.zonedLunchEnd = zonedLunchEnd;

        final ZoneOffset zoneOffSet = timezone.getOffset(date);

        this.workStart = TimeMachineService.getTimeWithOffset(zonedWorkStart, zoneOffSet);
        this.workEnd = TimeMachineService.getTimeWithOffset(zonedWorkEnd, zoneOffSet);
        this.lunchStart = TimeMachineService.getTimeWithOffset(zonedLunchStart, zoneOffSet);
        this.lunchEnd = TimeMachineService.getTimeWithOffset(zonedLunchEnd, zoneOffSet);

        this.cityCode = cityCode;
        this.timezone = timezone;
        this.company = company;
        this.workerHoliday = workerHoliday;
        this.cityHoliday = cityHoliday;
    }

    public WorkerCurrentPreferencesVo(final Date date, final String workerInternalId, final String workerExternalId, final String workSsid,
                                      final LocalTime zonedWorkStart, final LocalTime zonedWorkEnd, final LocalTime zonedLunchStart, final LocalTime zonedLunchEnd,
                                      final String cityCode, final SupportedTimezone timezone, final Company company,
                                      final Boolean workerHoliday, final Boolean cityHoliday) {
        this(TimeMachineService.toLocalDate(date), workerInternalId, workerExternalId, workSsid,
                zonedWorkStart, zonedWorkEnd, zonedLunchStart, zonedLunchEnd,
                cityCode, timezone, company,
                workerHoliday, cityHoliday);
    }

    public String getWorkerInternalId() {
        return workerInternalId;
    }

    public String getWorkerExternalId() {
        return workerExternalId;
    }

    public String getWorkSsid() {
        return workSsid;
    }

    public LocalTime getZonedWorkStart() {
        return zonedWorkStart;
    }

    public LocalTime getZonedWorkEnd() {
        return zonedWorkEnd;
    }

    public LocalTime getZonedLunchStart() {
        return zonedLunchStart;
    }

    public LocalTime getZonedLunchEnd() {
        return zonedLunchEnd;
    }

    public String getCityCode() {
        return cityCode;
    }

    public SupportedTimezone getTimezone() {
        return timezone;
    }

    public Company getCompany() {
        return company;
    }

    public Boolean workToday() {
        return !TimeMachineService.isWeekend(date)
                && !workerHoliday
                && !cityHoliday;
    }

    public NonWorkingReason getNonWorkingReason() {
        NonWorkingReason result = NonWorkingReason.NONE;

        if (workerHoliday) {
            result = NonWorkingReason.WORKER_HOLIDAY;
        } else if (cityHoliday) {
            result = NonWorkingReason.CITY_HOLIDAY;
        } else if (TimeMachineService.isWeekend(date)) {
            result = NonWorkingReason.WEEKEND;
        }

        return result;
    }

    public Boolean isTimeToStartWorking(final LocalTime time) {
        return workToday()
                && time.isAfter(workStart);
    }

    public Boolean isTimeToEndWorking(final LocalTime time) {
        return workToday()
                && time.isAfter(workEnd);
    }

    public Boolean lunchToday() {
        return !TimeMachineService.isWeekend(date)
                && !workerHoliday
                && !cityHoliday
                && zonedLunchStart != null;
    }

    public Boolean isTimeToStartLunch(final LocalTime time) {
        return lunchToday()
                && time.isAfter(lunchStart);
    }

    public Boolean isTimeToEndLunch(final LocalTime time) {
        return lunchToday()
                && time.isAfter(lunchEnd);
    }

    public boolean canBeNotified(final LocalTime time) {
        return workToday() && isTimeToStartWorking(time) && !isTimeToEndWorking(time);
    }

    @Override
    public String toString() {
        return "WorkerCurrentPreferencesVo{" +
                "date=" + TimeMachineService.formatDate(date) +
                ", workerInternalId='" + workerInternalId + '\'' +
                ", workerExternalId='" + workerExternalId + '\'' +
                ", workSsid='" + workSsid + '\'' +
                ", zonedWorkStart=" + TimeMachineService.formatTimeSimple(zonedWorkStart) +
                ", zonedWorkEnd=" + TimeMachineService.formatTimeSimple(zonedWorkEnd) +
                ", zonedLunchStart=" + TimeMachineService.formatTimeSimple(zonedLunchStart) +
                ", zonedLunchEnd=" + TimeMachineService.formatTimeSimple(zonedLunchEnd) +
                ", cityCode='" + cityCode + '\'' +
                ", timezone='" + timezone + '\'' +
                ", company='" + company + '\'' +
                ", workerHoliday='" + workerHoliday + '\'' +
                ", cityHoliday='" + cityHoliday + '\'' +
                '}';
    }
}