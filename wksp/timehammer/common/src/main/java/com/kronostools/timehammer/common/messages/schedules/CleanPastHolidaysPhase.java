package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.Phase;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

@JsonDeserialize(builder = CleanPastHolidaysPhaseBuilder.class)
public class CleanPastHolidaysPhase extends Phase<SimpleResult> {
    CleanPastHolidaysPhase(final SimpleResult result, final String errorMessage) {
        super(result, errorMessage);
    }
}