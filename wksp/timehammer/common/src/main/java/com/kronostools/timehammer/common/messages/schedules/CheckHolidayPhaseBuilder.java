package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.PhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class CheckHolidayPhaseBuilder extends PhaseBuilder<SimpleResult, CheckHolidayPhaseBuilder> {
    private Boolean holiday;

    public static CheckHolidayPhase copyAndBuild(final CheckHolidayPhase checkHolidayPhase) {
        return Optional.ofNullable(checkHolidayPhase)
                .map(chp -> CheckHolidayPhaseBuilder.copy(chp).build())
                .orElse(null);
    }

    public static CheckHolidayPhaseBuilder copy(final CheckHolidayPhase checkHolidayPhase) {
        return Optional.ofNullable(checkHolidayPhase)
                .map(chp -> new CheckHolidayPhaseBuilder()
                        .result(chp.getResult())
                        .errorMessage(chp.getErrorMessage())
                        .holiday(chp.getHoliday()))
                .orElse(null);
    }

    public CheckHolidayPhaseBuilder holiday(final Boolean holiday) {
        this.holiday = holiday;
        return this;
    }

    public CheckHolidayPhase build() {
        final CheckHolidayPhase chp = new CheckHolidayPhase(result, errorMessage);
        chp.setHoliday(holiday);

        return chp;
    }
}