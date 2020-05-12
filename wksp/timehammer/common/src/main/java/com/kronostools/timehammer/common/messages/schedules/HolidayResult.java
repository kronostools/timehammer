package com.kronostools.timehammer.common.messages.schedules;

public class HolidayResult extends Result {
    private Boolean holiday;

    public static class Builder implements ResultBuilder<HolidayResult> {
        private Boolean holiday;

        Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public Builder holiday(final Boolean holiday) {
            this.holiday = holiday;
            return this;
        }

        public HolidayResult build() {
            final HolidayResult result = new HolidayResult();
            result.setSuccessful(true);
            result.setHoliday(holiday);

            return result;
        }

        @Override
        public HolidayResult buildUnsuccessful(String errorMessage) {
            final HolidayResult result = new HolidayResult();
            result.setSuccessful(false);
            result.setErrorMessage(errorMessage);

            return result;
        }
    }

    public Boolean getHoliday() {
        return holiday;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }
}
