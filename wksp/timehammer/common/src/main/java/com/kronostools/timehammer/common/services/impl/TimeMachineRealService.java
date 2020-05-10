package com.kronostools.timehammer.common.services.impl;

import com.kronostools.timehammer.common.constants.SupportedTimezone;

import java.time.LocalDateTime;

public class TimeMachineRealService extends TimeMachineAbstractService {

    @Override
    public LocalDateTime getNow() {
        return now();
    }

    @Override
    public void timeTravelToDateTimeWithZone(final LocalDateTime newTimestamp, final SupportedTimezone zone) {
        LOG.info("Ignored time travel because time machine service is not mocked");
    }

    @Override
    public boolean isMocked() {
        return false;
    }
}