package com.kronostools.timehammer.common.services.impl;

import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.services.TimeMachineService;

import java.time.LocalDateTime;

public abstract class TimeMachineAbstractService implements TimeMachineService {
    LocalDateTime now() {
        return LocalDateTime.now(SupportedTimezone.UTC.getZone());
    }

    @Override
    public SupportedTimezone getTimezone() {
        return SupportedTimezone.UTC;
    }
}