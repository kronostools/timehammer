package com.kronostools.timehammer.dto;

public class DemoWorkerStatusForm {
    private String externalId;
    private String timestamp;
    private String dayOfWeek;
    private String work;
    private String lunch;
    private String status;
    private String holidays;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHolidays() {
        return holidays;
    }

    public void setHolidays(String holidays) {
        this.holidays = holidays;
    }

    @Override
    public String toString() {
        return "DemoWorkerStatusForm{" +
                "externalId='" + externalId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", work='" + work + '\'' +
                ", lunch='" + lunch + '\'' +
                ", status='" + status + '\'' +
                ", holidays=['" + holidays + "]'" +
                '}';
    }
}