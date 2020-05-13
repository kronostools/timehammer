package com.kronostools.timehammer.common.messages.schedules;

public class CheckHolidayResult extends Result {
    private Boolean holiday;

    public static class Builder implements ResultBuilder<CheckHolidayResult> {
        private Boolean holiday;

        Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public Builder holiday(final Boolean holiday) {
            this.holiday = holiday;
            return this;
        }

        public CheckHolidayResult build() {
            final CheckHolidayResult result = new CheckHolidayResult();
            result.setSuccessful(true);
            result.setHoliday(holiday);

            return result;
        }

        @Override
        public CheckHolidayResult buildUnsuccessful(String errorMessage) {
            final CheckHolidayResult result = new CheckHolidayResult();
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