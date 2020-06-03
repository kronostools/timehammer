package com.kronostools.timehammer.common.messages.registration.forms;

import com.kronostools.timehammer.common.constants.Company;

import java.util.List;

public class RegistrationRequestForm {
    private String workerInternalId;
    private Company company;
    private String workerExternalId;
    private String workerExternalPassword;
    private String workCity;
    private String workSsid;
    private RawTimetable defaultTimetable;
    private List<RawTimetableRange> customTimetables;

    public String getWorkerInternalId() {
        return workerInternalId;
    }

    public void setWorkerInternalId(String workerInternalId) {
        this.workerInternalId = workerInternalId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getWorkerExternalId() {
        return workerExternalId;
    }

    public void setWorkerExternalId(String workerExternalId) {
        this.workerExternalId = workerExternalId;
    }

    public String getWorkerExternalPassword() {
        return workerExternalPassword;
    }

    public void setWorkerExternalPassword(String workerExternalPassword) {
        this.workerExternalPassword = workerExternalPassword;
    }

    public String getWorkCity() {
        return workCity;
    }

    public void setWorkCity(String workCity) {
        this.workCity = workCity;
    }

    public String getWorkSsid() {
        return workSsid;
    }

    public void setWorkSsid(String workSsid) {
        this.workSsid = workSsid;
    }

    public RawTimetable getDefaultTimetable() {
        return defaultTimetable;
    }

    public void setDefaultTimetable(RawTimetable defaultTimetable) {
        this.defaultTimetable = defaultTimetable;
    }

    public List<RawTimetableRange> getCustomTimetables() {
        return customTimetables;
    }

    public void setCustomTimetables(List<RawTimetableRange> customTimetables) {
        this.customTimetables = customTimetables;
    }
}