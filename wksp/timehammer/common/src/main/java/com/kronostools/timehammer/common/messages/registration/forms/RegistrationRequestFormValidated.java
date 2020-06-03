package com.kronostools.timehammer.common.messages.registration.forms;

import com.kronostools.timehammer.common.constants.Company;

import java.util.List;

public class RegistrationRequestFormValidated {
    private String workerInternalId;
    private Company company;
    private String workerExternalId;
    private String workerExternalPassword;
    private String workCityCode;
    private String workSsid;
    private Timetable defaultTimetable;
    private List<TimetableRange> customTimetables;

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

    public String getWorkCityCode() {
        return workCityCode;
    }

    public void setWorkCityCode(String workCityCode) {
        this.workCityCode = workCityCode;
    }

    public String getWorkSsid() {
        return workSsid;
    }

    public void setWorkSsid(String workSsid) {
        this.workSsid = workSsid;
    }

    public Timetable getDefaultTimetable() {
        return defaultTimetable;
    }

    public void setDefaultTimetable(Timetable defaultTimetable) {
        this.defaultTimetable = defaultTimetable;
    }

    public List<TimetableRange> getCustomTimetables() {
        return customTimetables;
    }

    public void setCustomTimetables(List<TimetableRange> customTimetables) {
        this.customTimetables = customTimetables;
    }
}
