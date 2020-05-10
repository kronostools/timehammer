package com.kronostools.timehammer.comunytek.model;

public class HolidayResponse {
    private boolean successful;
    private boolean holiday;

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }
}
