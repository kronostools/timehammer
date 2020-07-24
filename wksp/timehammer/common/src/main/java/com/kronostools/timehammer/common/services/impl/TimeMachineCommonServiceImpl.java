package com.kronostools.timehammer.common.services.impl;

import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.services.TimeMachineServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public abstract class TimeMachineCommonServiceImpl implements TimeMachineServiceImpl {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    LocalDateTime now() {
        return LocalDateTime.now(SupportedTimezone.UTC.getZone());
    }
}