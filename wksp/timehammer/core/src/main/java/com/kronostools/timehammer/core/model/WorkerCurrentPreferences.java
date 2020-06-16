package com.kronostools.timehammer.core.model;

import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public class WorkerCurrentPreferences {
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

    public Boolean workToday() {
        return !CommonDateTimeUtils.isWeekend(date)
                && !workerHoliday
                && !cityHoliday;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public String getWorkSsid() {
        return workSsid;
    }

    public void setWorkSsid(String workSsid) {
        this.workSsid = workSsid;
    }

    public LocalTime getWorkStart() {
        return workStart;
    }

    public void setWorkStart(LocalTime workStart) {
        this.workStart = workStart;
    }

    public LocalTime getWorkEnd() {
        return workEnd;
    }

    public void setWorkEnd(LocalTime workEnd) {
        this.workEnd = workEnd;
    }

    public LocalTime getLunchStart() {
        return lunchStart;
    }

    public void setLunchStart(LocalTime lunchStart) {
        this.lunchStart = lunchStart;
    }

    public LocalTime getLunchEnd() {
        return lunchEnd;
    }

    public void setLunchEnd(LocalTime lunchEnd) {
        this.lunchEnd = lunchEnd;
    }

    public String getWorkCityCode() {
        return workCityCode;
    }

    public void setWorkCityCode(String workCityCode) {
        this.workCityCode = workCityCode;
    }

    public SupportedTimezone getTimezone() {
        return timezone;
    }

    public void setTimezone(SupportedTimezone timezone) {
        this.timezone = timezone;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public boolean isWorkerHoliday() {
        return workerHoliday;
    }

    public void setWorkerHoliday(boolean workerHoliday) {
        this.workerHoliday = workerHoliday;
    }

    public boolean isCityHoliday() {
        return cityHoliday;
    }

    public void setCityHoliday(boolean cityHoliday) {
        this.cityHoliday = cityHoliday;
    }

    public Set<String> getChatIds() {
        return chatIds;
    }

    public void setChatIds(Set<String> chatIds) {
        this.chatIds = chatIds;
    }
}