package com.kronostools.timehammer.dto;

public class NewSsidTrackingReportDto {
    private String ssid;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    @Override
    public String toString() {
        return "NewSsidTrackingReportDto{" +
                "ssid='" + ssid + '\'' +
                '}';
    }
}