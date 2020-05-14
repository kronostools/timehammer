package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = CheckHolidayPhaseBuilder.class)
public class CheckHolidayPhase extends Phase {
    private Boolean holiday;

    CheckHolidayPhase(final String errorMessage) {
        super(errorMessage);
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }
}