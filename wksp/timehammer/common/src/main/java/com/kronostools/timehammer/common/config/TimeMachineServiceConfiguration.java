package com.kronostools.timehammer.common.config;

import com.kronostools.timehammer.common.service.TimeMachineService;
import io.quarkus.arc.DefaultBean;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class TimeMachineServiceConfiguration {
    @Produces
    @DefaultBean
    public TimeMachineService timeMachineService() {
        return new TimeMachineService();
    }
}