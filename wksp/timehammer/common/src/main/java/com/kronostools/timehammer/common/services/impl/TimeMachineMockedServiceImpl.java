package com.kronostools.timehammer.common.services.impl;

import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;

import java.time.LocalDateTime;

public class TimeMachineMockedServiceImpl extends TimeMachineCommonServiceImpl {
    private LocalDateTime now;

    public TimeMachineMockedServiceImpl() {
        this.now = now();
    }

    @Override
    public LocalDateTime getNow() {
        LOG.trace("Getting current timestamp ({}) from timemachine", CommonDateTimeUtils.formatDateTimeToLog(now));
        return now;
    }

    @Override
    public void timeTravelToDateTimeWithZone(final LocalDateTime newTimestamp, final SupportedTimezone zone) {
        LOG.trace("Travelling in time to {} at {}", CommonDateTimeUtils.formatDateTimeToLog(newTimestamp), CommonDateTimeUtils.formatTimezoneToLog(zone));
        now = CommonDateTimeUtils.getDateTimeWithZone(newTimestamp, zone);
        LOG.info("Traveled in time to {} at UTC", CommonDateTimeUtils.formatDateTimeToLog(now));
    }
}