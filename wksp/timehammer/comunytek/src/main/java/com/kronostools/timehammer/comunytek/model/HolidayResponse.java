package com.kronostools.timehammer.comunytek.model;

import java.time.LocalDate;

public class HolidayResponse {
    private final boolean successful;
    private final Boolean holiday;
    private final LocalDate date;

    private HolidayResponse(final boolean successful) {
        this.successful = successful;
        this.holiday = null;
        this.date = null;
    }

    private HolidayResponse(final boolean successful, final boolean holiday, final LocalDate date) {
        this.successful = successful;
        this.holiday = holiday;
        this.date = date;
    }

    public static class Builder {
        private Boolean holiday;
        private LocalDate date;

        private Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public static HolidayResponse buildUnsuccessful() {
            return new HolidayResponse(false);
        }

        public Builder holiday(final boolean holiday) {
            this.holiday = holiday;
            return this;
        }

        public Builder date(final LocalDate date) {
            this.date = date;
            return this;
        }

        public HolidayResponse build() {
            return new HolidayResponse(true, holiday, date);
        }
    }

    public boolean isSuccessful() {
        return successful;
    }

    public Boolean isHoliday() {
        return holiday;
    }

    public LocalDate getDate() {
        return date;
    }
}
