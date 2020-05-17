package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = CleanPastHolidaysPhaseBuilder.class)
public class CleanPastHolidaysPhase extends Phase {
    CleanPastHolidaysPhase(final String errorMessage) {
        super(errorMessage);
    }
}