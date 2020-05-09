package com.kronostools.timehammer.common.services;

import com.kronostools.timehammer.common.constants.SupportedTimezone;

import java.time.LocalDateTime;

public interface TimeMachineService {
    LocalDateTime getNow();

    SupportedTimezone getTimezone();

    void timeTravelToDateTimeWithZone(final LocalDateTime newTimestamp, final SupportedTimezone zone);

    boolean isMocked();
}
