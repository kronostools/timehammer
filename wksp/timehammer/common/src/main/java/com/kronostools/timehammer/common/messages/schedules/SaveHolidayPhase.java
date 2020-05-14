package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = SaveHolidayPhaseBuilder.class)
public class SaveHolidayPhase extends Phase {
    SaveHolidayPhase(final String errorMessage) {
        super(errorMessage);
    }
}