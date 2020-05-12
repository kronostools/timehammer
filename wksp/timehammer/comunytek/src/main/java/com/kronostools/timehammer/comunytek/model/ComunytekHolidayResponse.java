package com.kronostools.timehammer.comunytek.model;

import java.time.LocalDate;

public class ComunytekHolidayResponse extends ComunytekResponse {
    private final Boolean holiday;
    private final LocalDate date;

    ComunytekHolidayResponse(final String errorMessage) {
        super(false, errorMessage);
        this.holiday = null;
        this.date = null;
    }

    ComunytekHolidayResponse(final boolean holiday, final LocalDate date) {
        super(true, null);
        this.holiday = holiday;
        this.date = date;
    }

    public static class Builder implements ComunytekResponseBuilder<ComunytekHolidayResponse> {
        private Boolean holiday;
        private LocalDate date;

        private Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public Builder holiday(final boolean holiday) {
            this.holiday = holiday;
            return this;
        }

        public Builder date(final LocalDate date) {
            this.date = date;
            return this;
        }

        public ComunytekHolidayResponse build() {
            return new ComunytekHolidayResponse(holiday, date);
        }

        public ComunytekHolidayResponse buildUnsuccessful(final String errorMessage) {
            return new ComunytekHolidayResponse(errorMessage);
        }

    }

    public Boolean isHoliday() {
        return holiday;
    }

    public LocalDate getDate() {
        return date;
    }
}
