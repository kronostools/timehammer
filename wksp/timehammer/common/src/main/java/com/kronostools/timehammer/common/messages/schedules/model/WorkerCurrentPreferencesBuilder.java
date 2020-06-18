package com.kronostools.timehammer.common.messages.schedules.model;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.constants.SupportedTimezone;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@JsonPOJOBuilder(withPrefix = "")
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

    public WorkerCurrentPreferencesBuilder() {
        this.chatIds = new HashSet<>();
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