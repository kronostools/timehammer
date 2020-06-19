package com.kronostools.timehammer.statemachine.service;

import com.kronostools.timehammer.common.constants.WorkerStatusAction;
import com.kronostools.timehammer.common.messages.constants.WorkerStatusContext;
import com.kronostools.timehammer.common.messages.schedules.model.WorkerCurrentPreferences;
import com.kronostools.timehammer.common.services.TimeMachineService;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalTime;

@ApplicationScoped
public class StateMachineService {
    private final TimeMachineService timeMachineService;

    public StateMachineService(final TimeMachineService timeMachineService) {
        this.timeMachineService = timeMachineService;
    }

    public Uni<WorkerStatusAction> getAction(final WorkerCurrentPreferences currentPreferences, final WorkerStatusContext statusContext) {
        final LocalTime now = timeMachineService.getNow().toLocalTime();

        WorkerStatusAction statusAction = WorkerStatusAction.NOOP;

        switch (statusContext) {
            case BEFORE_WORK:
                if (currentPreferences.isTimeToStartWorking(now)) {
                    statusAction = WorkerStatusAction.CLOCKIN;
                }
                break;
            case WORK:
                if (currentPreferences.isTimeToStartLunch(now)) {
                    statusAction = WorkerStatusAction.CLOCKIN;
                } else if (currentPreferences.isTimeToEndWorking(now)) {
                    statusAction = WorkerStatusAction.CLOCKOUT;
                }
                break;
            case LUNCH:
                if (currentPreferences.isTimeToEndLunch(now)) {
                    statusAction = WorkerStatusAction.CLOCKOUT;
                }
                break;
        }

        return Uni.createFrom().item(statusAction);
    }
}