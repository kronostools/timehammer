package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonPOJOBuilder(withPrefix = "")
public class CheckHolidayPhaseBuilder extends PhaseBuilder<CheckHolidayPhaseBuilder> {
    private Boolean holiday;

    public static CheckHolidayPhaseBuilder copy(final CheckHolidayPhase checkHolidayPhase) {
        return new CheckHolidayPhaseBuilder()
                .errorMessage(checkHolidayPhase.getErrorMessage())
                .holiday(checkHolidayPhase.isHoliday());
    }

    public CheckHolidayPhaseBuilder holiday(final Boolean holiday) {
        this.holiday = holiday;
        return this;
    }

    public CheckHolidayPhase build() {
        final CheckHolidayPhase result = new CheckHolidayPhase(errorMessage);
        result.setHoliday(holiday);

        return result;
    }
}