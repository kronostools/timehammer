package com.kronostools.timehammer.common.service;

import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.utils.CommonUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeMachineService {
    private LocalDateTime now;

    public TimeMachineService() {
        this.now = now();
    }

    public LocalDateTime getNow() {
        if (isTimeMachineMocked()) {
            return now;
        } else {
            return now();
        }
    }

    public void timeTravelToDateTimeWithZone(final LocalDateTime newTimestamp, final SupportedTimezone zone) {
        now = newTimestamp.atOffset(zone.getOffset(newTimestamp)).withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    private LocalDateTime now() {
        return LocalDateTime.now(SupportedTimezone.UTC.getZone());
    }

    private boolean isTimeMachineMocked() {
        return CommonUtils.isDemoMode();
    }
}