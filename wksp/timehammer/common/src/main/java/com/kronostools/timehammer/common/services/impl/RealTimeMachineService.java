package com.kronostools.timehammer.common.services.impl;

import com.kronostools.timehammer.common.constants.SupportedTimezone;

import java.time.LocalDateTime;

public class RealTimeMachineService extends AbstractTimeMachineService {

    public LocalDateTime getNow() {
        return now();
    }

    public void timeTravelToDateTimeWithZone(final LocalDateTime newTimestamp, final SupportedTimezone zone) {
        // do nothing
    }
}