package com.kronostools.timehammer.common.services.impl;

import com.kronostools.timehammer.common.constants.SupportedTimezone;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class MockedTimeMachineService extends AbstractTimeMachineService {
    private LocalDateTime now;

    public MockedTimeMachineService() {
        this.now = now();
    }

    @Override
    public LocalDateTime getNow() {
        return now;
    }

    @Override
    public void timeTravelToDateTimeWithZone(final LocalDateTime newTimestamp, final SupportedTimezone zone) {
        now = newTimestamp.atOffset(zone.getOffset(newTimestamp)).withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    @Override
    public boolean isMocked() {
        return true;
    }
}