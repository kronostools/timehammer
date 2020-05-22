package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.PhaseBuilder;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class CleanPastHolidaysPhaseBuilder extends PhaseBuilder<SimpleResult, CleanPastHolidaysPhaseBuilder> {
    private CleanPastHolidaysPhase cleanPastHolidaysPhase;

    public static CleanPastHolidaysPhase copyAndBuild(final CleanPastHolidaysPhase cleanPashtHolidaysPhase) {
        return Optional.ofNullable(cleanPashtHolidaysPhase)
                .map(cphp -> CleanPastHolidaysPhaseBuilder.copy(cphp).build())
                .orElse(null);
    }

    public static CleanPastHolidaysPhaseBuilder copy(final CleanPastHolidaysPhase cleanPashtHolidaysPhase) {
        return Optional.ofNullable(cleanPashtHolidaysPhase)
                .map(cphp -> new CleanPastHolidaysPhaseBuilder()
                        .result(cphp.getResult())
                        .errorMessage(cphp.getErrorMessage()))
                .orElse(null);
    }

    public CleanPastHolidaysPhaseBuilder cleanPastHolidaysPhase(final CleanPastHolidaysPhase cleanPastHolidays) {
        this.cleanPastHolidaysPhase = cleanPastHolidays;
        return this;
    }

    public CleanPastHolidaysPhase build() {
        return new CleanPastHolidaysPhase(result, errorMessage);
    }
}