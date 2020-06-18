package com.kronostools.timehammer.statemachine.service;

import com.kronostools.timehammer.common.constants.WorkerStatusAction;
import com.kronostools.timehammer.common.messages.constants.WorkerStatusContext;
import com.kronostools.timehammer.common.messages.schedules.model.WorkerCurrentPreferences;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StateMachineService {
    public Uni<WorkerStatusAction> getAction(final WorkerCurrentPreferences workerCurrentPreferences, final WorkerStatusContext workerStatusContext) {
        return Uni.createFrom().item(WorkerStatusAction.CLOCKIN);
    }
}