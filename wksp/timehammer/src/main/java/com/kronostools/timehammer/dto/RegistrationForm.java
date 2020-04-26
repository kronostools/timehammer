package com.kronostools.timehammer.dto;

import com.kronostools.timehammer.dto.form.TimetableForm;
import com.kronostools.timehammer.enums.Company;
import com.kronostools.timehammer.vo.CityVo;

import java.util.List;
import java.util.stream.Collectors;

public class RegistrationForm {
    private String registrationId;
    private Company company;
    private String externalId;
    private String externalPassword;
    private CityVo workCity;
    private String workSsid;
    private List<TimetableForm> timetables;

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getExternalPassword() {
        return externalPassword;
    }

    public void setExternalPassword(String externalPassword) {
        this.externalPassword = externalPassword;
    }

    public CityVo getWorkCity() {
        return workCity;
    }

    public void setWorkCity(CityVo workCity) {
        this.workCity = workCity;
    }

    public String getWorkSsid() {
        return workSsid;
    }

    public void setWorkSsid(String workSsid) {
        this.workSsid = workSsid;
    }

    public List<TimetableForm> getTimetables() {
        return timetables;
    }

    public void setTimetables(List<TimetableForm> timetables) {
        this.timetables = timetables;
    }

    @Override
    public String toString() {
        return "RegistrationForm{" +
                "registrationId='" + registrationId + '\'' +
                ", company='" + company + '\'' +
                ", externalId='" + externalId + '\'' +
                ", workCity='" + workCity + '\'' +
                ", workSsid='" + workSsid + '\'' +
                ", timetables=[" + timetables.stream().map(TimetableForm::toString).collect(Collectors.joining(", ")) +
                "]}";
    }
}