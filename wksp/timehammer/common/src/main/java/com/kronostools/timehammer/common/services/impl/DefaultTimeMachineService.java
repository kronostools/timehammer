package com.kronostools.timehammer.common.services.impl;

import com.kronostools.timehammer.common.constants.SupportedTimezone;
import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.common.services.TimeMachineServiceImpl;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;

public class DefaultTimeMachineService implements TimeMachineService {
    private final TimeMachineMockedServiceImpl timeMachineMockedService;
    private final TimeMachineRealService timeMachineRealService;

    @ConfigProperty(name = "timehammer.mocks.timemachineservice")
    Boolean timeMachineServiceMocked;

    public DefaultTimeMachineService(final TimeMachineMockedServiceImpl timeMachineMockedService, final TimeMachineRealService timeMachineRealService) {
        this.timeMachineMockedService = timeMachineMockedService;
        this.timeMachineRealService = timeMachineRealService;
    }

    @Override
    public SupportedTimezone getTimezone() {
        return SupportedTimezone.UTC;
    }

    @Override
    public boolean isMocked() {
        return timeMachineServiceMocked;
    }

    @Override
    public LocalDateTime getNow() {
        return getService().getNow();
    }

    @Override
    public void timeTravelToDateTimeWithZone(LocalDateTime newTimestamp, SupportedTimezone zone) {
        getService().timeTravelToDateTimeWithZone(newTimestamp, zone);
    }

    private TimeMachineServiceImpl getService() {
        if (isMocked()) {
            return timeMachineMockedService;
        } else {
            return timeMachineRealService;
        }
    }
}
