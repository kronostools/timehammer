package com.kronostools.timehammer.common.config;

import com.kronostools.timehammer.common.processors.TimeMachineProcessor;
import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.common.services.impl.TimeMachineMockedService;
import com.kronostools.timehammer.common.services.impl.TimeMachineRealService;
import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.arc.profile.UnlessBuildProfile;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class TimeMachineConfiguration {
    @Produces
    @ApplicationScoped
    @IfBuildProfile("prod")
    public TimeMachineService realTimeMachineService() {
        return new TimeMachineRealService();
    }

    @Produces
    @ApplicationScoped
    @UnlessBuildProfile("prod")
    public TimeMachineService mockedTimeMachineService() {
        return new TimeMachineMockedService();
    }

    @Produces
    @ApplicationScoped
    @DefaultBean
    public TimeMachineProcessor timeMachineProcessor(final TimeMachineService timeMachineService) {
        return new TimeMachineProcessor(timeMachineService);
    }
}