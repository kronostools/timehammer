package com.kronostools.timehammer.common.services;

import com.kronostools.timehammer.common.constants.SupportedTimezone;

import java.time.LocalDateTime;

public interface TimeMachineService {
    SupportedTimezone getTimezone();

    boolean isMocked();

    LocalDateTime getNow();

    void timeTravelToDateTimeWithZone(final LocalDateTime newTimestamp, final SupportedTimezone zone);
}
