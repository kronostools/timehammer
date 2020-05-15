package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class CheckHolidayPhaseBuilder extends PhaseBuilder<CheckHolidayPhaseBuilder> {
    private Boolean holiday;

    public static CheckHolidayPhase copyAndBuild(final CheckHolidayPhase checkHolidayPhase) {
        return Optional.ofNullable(checkHolidayPhase)
                .map(chp -> CheckHolidayPhaseBuilder.copy(chp).build())
                .orElse(null);
    }

    public static CheckHolidayPhaseBuilder copy(final CheckHolidayPhase checkHolidayPhase) {
        return Optional.ofNullable(checkHolidayPhase)
                .map(chp -> new CheckHolidayPhaseBuilder()
                        .errorMessage(chp.getErrorMessage())
                        .holiday(chp.isHoliday()))
                .orElse(null);
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