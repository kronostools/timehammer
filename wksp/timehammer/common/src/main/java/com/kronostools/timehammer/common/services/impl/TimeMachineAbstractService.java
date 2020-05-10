package com.kronostools.timehammer.common.services.impl;

import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.services.TimeMachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public abstract class TimeMachineAbstractService implements TimeMachineService {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    LocalDateTime now() {
        return LocalDateTime.now(SupportedTimezone.UTC.getZone());
    }

    @Override
    public SupportedTimezone getTimezone() {
        return SupportedTimezone.UTC;
    }
}