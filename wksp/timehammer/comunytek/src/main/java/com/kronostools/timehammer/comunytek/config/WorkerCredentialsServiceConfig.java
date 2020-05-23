package com.kronostools.timehammer.comunytek.config;

import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.comunytek.service.WorkerCredentialsService;
import com.kronostools.timehammer.comunytek.service.WorkerCredentialsServiceMocked;
import com.kronostools.timehammer.comunytek.service.WorkerCredentialsServiceReal;
import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.profile.UnlessBuildProfile;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class WorkerCredentialsServiceConfig {
    @Produces
    @ApplicationScoped
    @DefaultBean
    public WorkerCredentialsService realWorkerCredentialsService(final TimeMachineService timeMachineService) {
        return new WorkerCredentialsServiceReal(timeMachineService);
    }

    @Produces
    @ApplicationScoped
    @UnlessBuildProfile("prod")
    public WorkerCredentialsService mockedWorkerCredentialsService(final TimeMachineService timeMachineService) {
        return new WorkerCredentialsServiceMocked(timeMachineService);
    }
}