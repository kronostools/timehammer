package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@JsonDeserialize(builder = CleanPastWorkersHolidaysBuilder.class)
public class CleanPastWorkersHolidays extends ProcessableScheduleMessage {
    private CleanPastHolidaysPhase cleanPastHolidaysPhase;

    CleanPastWorkersHolidays(final LocalDateTime timestamp, final String name, final UUID executionId) {
        super(timestamp, name, executionId);
    }

    public boolean processedSuccessfully() {
        return Optional.ofNullable(cleanPastHolidaysPhase)
                .map(CleanPastHolidaysPhase::isSuccessful)
                .orElse(false);
    }

    public CleanPastHolidaysPhase getCleanPastHolidaysPhase() {
        return cleanPastHolidaysPhase;
    }

    public void setCleanPastHolidaysPhase(CleanPastHolidaysPhase cleanPastHolidaysPhase) {
        this.cleanPastHolidaysPhase = cleanPastHolidaysPhase;
    }
}