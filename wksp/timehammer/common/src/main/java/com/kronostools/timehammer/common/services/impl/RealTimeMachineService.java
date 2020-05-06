package com.kronostools.timehammer.common.services.impl;

import com.kronostools.timehammer.common.constants.SupportedTimezone;

import java.time.LocalDateTime;

public class RealTimeMachineService extends AbstractTimeMachineService {

    @Override
    public LocalDateTime getNow() {
        return now();
    }

    @Override
    public void timeTravelToDateTimeWithZone(final LocalDateTime newTimestamp, final SupportedTimezone zone) {
        // do nothing
    }

    @Override
    public boolean isMocked() {
        return false;
    }
}