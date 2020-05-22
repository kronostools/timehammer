package com.kronostools.timehammer.common.messages.schedules;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.Phase;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;

@JsonDeserialize(builder = CheckHolidayPhaseBuilder.class)
public class CheckHolidayPhase extends Phase<SimpleResult> {
    private Boolean holiday;

    CheckHolidayPhase(final SimpleResult result, final String errorMessage) {
        super(result, errorMessage);
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }
}