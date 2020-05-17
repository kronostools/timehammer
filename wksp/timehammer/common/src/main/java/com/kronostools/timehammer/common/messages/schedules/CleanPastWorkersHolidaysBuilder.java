package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class CleanPastWorkersHolidaysBuilder extends ProcessableScheduleMessageBuilder<CleanPastWorkersHolidaysBuilder> {
    private CleanPastHolidaysPhase cleanPastHolidaysPhase;

    public static CleanPastWorkersHolidays copyAndBuild(final CleanPastWorkersHolidays cleanPastWorkersHolidays) {
        return Optional.ofNullable(cleanPastWorkersHolidays)
                .map(cph -> CleanPastWorkersHolidaysBuilder.copy(cph).build())
                .orElse(null);
    }

    public static CleanPastWorkersHolidaysBuilder copy(final CleanPastWorkersHolidays cleanPastWorkersHolidays) {
        return Optional.ofNullable(cleanPastWorkersHolidays)
                .map(cph -> new CleanPastWorkersHolidaysBuilder()
                    .generated(cph.getGenerated())
                    .executionId(cph.getExecutionId())
                    .name(cph.getName())
                    .cleanPastHolidaysPhase(CleanPastHolidaysPhaseBuilder.copyAndBuild(cph.getCleanPastHolidaysPhase())))
                .orElse(null);
    }

    public CleanPastWorkersHolidaysBuilder cleanPastHolidaysPhase(final CleanPastHolidaysPhase cleanPastHolidaysPhase) {
        this.cleanPastHolidaysPhase = cleanPastHolidaysPhase;
        return this;
    }

    public CleanPastWorkersHolidays build() {
        final CleanPastWorkersHolidays result = new CleanPastWorkersHolidays(generated, name, executionId);
        result.setCleanPastHolidaysPhase(cleanPastHolidaysPhase);

        return result;
    }
}