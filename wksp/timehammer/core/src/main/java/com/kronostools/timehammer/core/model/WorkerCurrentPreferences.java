package com.kronostools.timehammer.core.model;

import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.constants.NonWorkingReason;
import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import io.vertx.mutiny.sqlclient.Row;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

public class WorkerCurrentPreferences {
    private final LocalDate date;
    private final String workerInternalId;
    private final String workerExternalId;
    private final String workSsid;
    private final LocalTime workStart;
    private final LocalTime workEnd;
    private final LocalTime lunchStart;
    private final LocalTime lunchEnd;
    private final String workCityCode;
    private final SupportedTimezone timezone;
    private final Company company;
    private final Boolean workerHoliday;
    private final Boolean cityHoliday;

    public WorkerCurrentPreferences(final LocalDate date, final String workerInternalId, final String workerExternalId, final String workSsid,
                                    final LocalTime workStart, final LocalTime workEnd, final LocalTime lunchStart, final LocalTime lunchEnd,
                                    final String workCityCode, final SupportedTimezone timezone, final Company company,
                                    final Boolean workerHoliday, final Boolean cityHoliday) {
        this.date = date;
        this.workerInternalId = workerInternalId;
        this.workerExternalId = workerExternalId;
        this.workSsid = workSsid;
        this.workStart = workStart;
        this.workEnd = workEnd;
        this.lunchStart = lunchStart;
        this.lunchEnd = lunchEnd;
        this.workCityCode = workCityCode;
        this.timezone = timezone;
        this.company = company;
        this.workerHoliday = workerHoliday;
        this.cityHoliday = cityHoliday;
    }

    public static WorkerCurrentPreferences from(final LocalDate date, final Row row) {
        final String workerInternalId = row.getString("workerInternalId");
        final String workerExternalId = row.getString("workerExternalId");
        final String workSsid = row.getString("workSsid");

        final SupportedTimezone timezone = SupportedTimezone.fromTimezoneName(row.getString("timezone"));

        final ZoneOffset zoneOffSet = timezone.getOffset(date);

        final LocalTime workStart = CommonDateTimeUtils.getTimeWithOffset(row.getLocalTime("workStart"), zoneOffSet);
        final LocalTime workEnd = CommonDateTimeUtils.getTimeWithOffset(row.getLocalTime("workEnd"), zoneOffSet);
        final LocalTime lunchStart = CommonDateTimeUtils.getTimeWithOffset(row.getLocalTime("lunchStart"), zoneOffSet);
        final LocalTime lunchEnd = CommonDateTimeUtils.getTimeWithOffset(row.getLocalTime("lunchEnd"), zoneOffSet);
        final String cityCode = row.getString("workCityCode");
        final Company company = Company.fromCode(row.getString("companyCode"));
        final Boolean workerHoliday = row.getBoolean("workerHoliday");
        final Boolean cityHoliday = row.getBoolean("cityHoliday");

        return new WorkerCurrentPreferences(date, workerInternalId, workerExternalId, workSsid,
                workStart, workEnd, lunchStart, lunchEnd,
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

    public String getWorkCityCode() {
        return workCityCode;
    }

    public SupportedTimezone getTimezone() {
        return timezone;
    }

    public Company getCompany() {
        return company;
    }

    public Boolean workToday() {
        return !CommonDateTimeUtils.isWeekend(date)
                && !workerHoliday
                && !cityHoliday;
    }

    public NonWorkingReason getNonWorkingReason() {
        NonWorkingReason result = NonWorkingReason.NONE;

        if (workerHoliday) {
            result = NonWorkingReason.WORKER_HOLIDAY;
        } else if (cityHoliday) {
            result = NonWorkingReason.CITY_HOLIDAY;
        } else if (CommonDateTimeUtils.isWeekend(date)) {
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
        return !CommonDateTimeUtils.isWeekend(date)
                && !workerHoliday
                && !cityHoliday
                && lunchStart != null;
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
}