package com.kronostools.timehammer.core.model;

import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import io.vertx.mutiny.sqlclient.Row;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

public class WorkerCurrentPreferencesBuilder {
    private LocalDate date;
    private String workerInternalId;
    private String workerExternalId;
    private String workSsid;
    private LocalTime workStart;
    private LocalTime workEnd;
    private LocalTime lunchStart;
    private LocalTime lunchEnd;
    private String workCityCode;
    private SupportedTimezone timezone;
    private Company company;
    private boolean workerHoliday;
    private boolean cityHoliday;
    private Set<String> chatIds;

    WorkerCurrentPreferencesBuilder() {
        this.chatIds = new HashSet<>();
    }

    public static WorkerCurrentPreferencesBuilder from(final LocalDate date, final Row row) {
        final String workerInternalId = row.getString("worker_internal_id");
        final String workerExternalId = row.getString("worker_external_id");
        final String workSsid = row.getString("work_ssid");

        final SupportedTimezone timezone = SupportedTimezone.fromTimezoneName(row.getString("timezone"));

        final ZoneOffset zoneOffSet = timezone.getOffset(date);

        final LocalTime workStart = CommonDateTimeUtils.getTimeWithOffset(row.getLocalTime("work_start"), zoneOffSet);
        final LocalTime workEnd = CommonDateTimeUtils.getTimeWithOffset(row.getLocalTime("work_end"), zoneOffSet);
        final LocalTime lunchStart = CommonDateTimeUtils.getTimeWithOffset(row.getLocalTime("lunch_start"), zoneOffSet);
        final LocalTime lunchEnd = CommonDateTimeUtils.getTimeWithOffset(row.getLocalTime("lunch_end"), zoneOffSet);
        final String cityCode = row.getString("work_city_code");
        final Company company = Company.fromCode(row.getString("company_code"));
        final Boolean workerHoliday = row.getBoolean("worker_holiday");
        final Boolean cityHoliday = row.getBoolean("city_holiday");
        final String chatId = row.getString("chat_id");

        return new WorkerCurrentPreferencesBuilder()
            .date(date)
            .workerInternalId(workerInternalId)
            .workerExternalId(workerExternalId)
            .workSsid(workSsid)
            .workStart(workStart)
            .workEnd(workEnd)
            .lunchStart(lunchStart)
            .lunchEnd(lunchEnd)
            .workCityCode(cityCode)
            .timezone(timezone)
            .company(company)
            .workerHoliday(workerHoliday)
            .cityHoliday(cityHoliday)
            .addChatId(chatId);
    }

    public WorkerCurrentPreferencesBuilder date(final LocalDate date) {
        this.date = date;
        return this;
    }

    public String getWorkerInternalId() {
        return workerInternalId;
    }

    public WorkerCurrentPreferencesBuilder workerInternalId(final String workerInternalId) {
        this.workerInternalId = workerInternalId;
        return this;
    }

    public WorkerCurrentPreferencesBuilder workerExternalId(final String workerExternalId) {
        this.workerExternalId = workerExternalId;
        return this;
    }

    public WorkerCurrentPreferencesBuilder workSsid(final String workSsid) {
        this.workSsid = workSsid;
        return this;
    }

    public WorkerCurrentPreferencesBuilder workStart(final LocalTime workStart) {
        this.workStart = workStart;
        return this;
    }

    public WorkerCurrentPreferencesBuilder workEnd(final LocalTime workEnd) {
        this.workEnd = workEnd;
        return this;
    }

    public WorkerCurrentPreferencesBuilder lunchStart(final LocalTime lunchStart) {
        this.lunchStart = lunchStart;
        return this;
    }

    public WorkerCurrentPreferencesBuilder lunchEnd(final LocalTime lunchEnd) {
        this.lunchEnd = lunchEnd;
        return this;
    }

    public WorkerCurrentPreferencesBuilder workCityCode(final String workCityCode) {
        this.workCityCode = workCityCode;
        return this;
    }

    public WorkerCurrentPreferencesBuilder timezone(final SupportedTimezone timezone) {
        this.timezone = timezone;
        return this;
    }

    public WorkerCurrentPreferencesBuilder company(final Company company) {
        this.company = company;
        return this;
    }

    public WorkerCurrentPreferencesBuilder workerHoliday(final boolean workerHoliday) {
        this.workerHoliday = workerHoliday;
        return this;
    }

    public WorkerCurrentPreferencesBuilder cityHoliday(final boolean cityHoliday) {
        this.cityHoliday = cityHoliday;
        return this;
    }

    public WorkerCurrentPreferencesBuilder addChatId(final String chatId) {
        if (chatIds == null) {
            chatIds = new HashSet<>();
        }

        chatIds.add(chatId);

        return this;
    }

    public WorkerCurrentPreferences build() {
        final WorkerCurrentPreferences result = new WorkerCurrentPreferences();
        result.setDate(date);
        result.setWorkerInternalId(workerInternalId);
        result.setWorkerExternalId(workerExternalId);
        result.setWorkSsid(workSsid);
        result.setWorkStart(workStart);
        result.setWorkEnd(workEnd);
        result.setLunchStart(lunchStart);
        result.setLunchEnd(lunchEnd);
        result.setWorkCityCode(workCityCode);
        result.setTimezone(timezone);
        result.setCompany(company);
        result.setWorkerHoliday(workerHoliday);
        result.setCityHoliday(cityHoliday);
        result.setChatIds(chatIds);

        return result;
    }
}