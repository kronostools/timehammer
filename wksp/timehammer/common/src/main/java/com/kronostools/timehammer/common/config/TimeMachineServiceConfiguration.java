package com.kronostools.timehammer.common.config;

import com.kronostools.timehammer.common.processors.TimeMachineProcessor;
import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.common.services.impl.MockedTimeMachineService;
import com.kronostools.timehammer.common.services.impl.RealTimeMachineService;
import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.profile.UnlessBuildProfile;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class TimeMachineServiceConfiguration {
    @Produces
    @DefaultBean
    public TimeMachineService realTimeMachineService() {
        return new RealTimeMachineService();
    }

    @Produces
    @UnlessBuildProfile("prod")
    public TimeMachineService mockedTimeMachineService() {
        return new MockedTimeMachineService();
    }

    @Produces
    @DefaultBean
    public TimeMachineProcessor timeMachineProcessor(final TimeMachineService timeMachineService) {
        return new TimeMachineProcessor(timeMachineService);
    }
}