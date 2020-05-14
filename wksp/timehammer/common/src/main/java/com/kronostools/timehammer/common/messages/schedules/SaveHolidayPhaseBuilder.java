package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonPOJOBuilder(withPrefix = "")
public class SaveHolidayPhaseBuilder extends PhaseBuilder<SaveHolidayPhaseBuilder> {

    public static SaveHolidayPhaseBuilder copy(final SaveHolidayPhase saveHolidayResult) {
        return new SaveHolidayPhaseBuilder()
                .errorMessage(saveHolidayResult.getErrorMessage());
    }

    public SaveHolidayPhase build() {
        return new SaveHolidayPhase(errorMessage);
    }
}