package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.PhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class SaveHolidayPhaseBuilder extends PhaseBuilder<SimpleResult, SaveHolidayPhaseBuilder> {

    public static SaveHolidayPhase copyAndBuild(final SaveHolidayPhase saveHolidayPhase) {
        return Optional.ofNullable(saveHolidayPhase)
                .map(shp -> SaveHolidayPhaseBuilder.copy(shp).build())
                .orElse(null);
    }

    public static SaveHolidayPhaseBuilder copy(final SaveHolidayPhase saveHolidayPhase) {
        return Optional.ofNullable(saveHolidayPhase)
                .map(shp -> new SaveHolidayPhaseBuilder()
                        .result(shp.getResult())
                        .errorMessage(shp.getErrorMessage()))
                .orElse(null);
    }

    public SaveHolidayPhase build() {
        return new SaveHolidayPhase(result, errorMessage);
    }
}