package com.kronostools.timehammer.common.config;

import com.kronostools.timehammer.common.processors.TimeMachineProcessor;
import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.common.services.impl.DefaultTimeMachineService;
import com.kronostools.timehammer.common.services.impl.TimeMachineMockedServiceImpl;
import com.kronostools.timehammer.common.services.impl.TimeMachineRealService;
import io.quarkus.arc.DefaultBean;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class TimeMachineConfiguration {
    @ConfigProperty(name = "timehammer.mocks.timemachineservice", defaultValue = "false")
    Boolean timeMachineServiceMocked;

    @Produces
    @ApplicationScoped
    @DefaultBean
    public TimeMachineService timeMachineService() {
        return new DefaultTimeMachineService(new TimeMachineMockedServiceImpl(), new TimeMachineRealService(), timeMachineServiceMocked);
    }

    @Produces
    @ApplicationScoped
    @DefaultBean
    public TimeMachineProcessor timeMachineProcessor(final TimeMachineService timeMachineService) {
        return new TimeMachineProcessor(timeMachineService);
    }
}