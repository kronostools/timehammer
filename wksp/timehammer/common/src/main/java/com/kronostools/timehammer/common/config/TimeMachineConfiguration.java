package com.kronostools.timehammer.common.config;

import com.kronostools.timehammer.common.processors.TimeMachineProcessor;
import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.common.services.impl.TimeMachineMockedService;
import com.kronostools.timehammer.common.services.impl.TimeMachineRealService;
import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.profile.UnlessBuildProfile;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class TimeMachineConfiguration {
    @Produces
    @DefaultBean
    public TimeMachineService realTimeMachineService() {
        return new TimeMachineRealService();
    }

    @Produces
    @UnlessBuildProfile("prod")
    public TimeMachineService mockedTimeMachineService() {
        return new TimeMachineMockedService();
    }

    @Produces
    @DefaultBean
    public TimeMachineProcessor timeMachineProcessor(final TimeMachineService timeMachineService) {
        return new TimeMachineProcessor(timeMachineService);
    }
}