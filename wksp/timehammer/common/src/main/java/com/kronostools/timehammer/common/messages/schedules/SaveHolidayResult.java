package com.kronostools.timehammer.common.messages.schedules;

public class SaveHolidayResult extends Result {

    public static class Builder implements ResultBuilder<SaveHolidayResult> {

        Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public SaveHolidayResult build() {
            final SaveHolidayResult result = new SaveHolidayResult();
            result.setSuccessful(true);

            return result;
        }

        @Override
        public SaveHolidayResult buildUnsuccessful(String errorMessage) {
            final SaveHolidayResult result = new SaveHolidayResult();
            result.setSuccessful(false);
            result.setErrorMessage(errorMessage);

            return result;
        }
    }
}