package com.kronostools.timehammer.common.services.impl;

import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.services.TimeMachineService;

import java.time.LocalDateTime;

public abstract class AbstractTimeMachineService implements TimeMachineService {
    LocalDateTime now() {
        return LocalDateTime.now(SupportedTimezone.UTC.getZone());
    }
}