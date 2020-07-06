package com.kronostools.timehammer.statemachine.service;

import com.kronostools.timehammer.common.constants.WorkerStatusAction;
import com.kronostools.timehammer.common.messages.constants.WorkerStatusContext;
import com.kronostools.timehammer.common.messages.schedules.model.WorkerCurrentPreferences;
import com.kronostools.timehammer.common.services.TimeMachineService;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.LocalTime;

@ApplicationScoped
public class StateMachineService {
    private final TimeMachineService timeMachineService;
    private final WorkerWaitService workerWaitService;

    public StateMachineService(final TimeMachineService timeMachineService, final WorkerWaitService workerWaitService) {
        this.timeMachineService = timeMachineService;
        this.workerWaitService = workerWaitService;
    }

    public Uni<WorkerStatusAction> getAction(final WorkerCurrentPreferences currentPreferences, final WorkerStatusContext statusContext) {
        final LocalDateTime timestamp = timeMachineService.getNow();
        final LocalTime today = timestamp.toLocalTime();

        WorkerStatusAction statusAction = WorkerStatusAction.NOOP;

        switch (statusContext) {
            case BEFORE_WORK:
                if (currentPreferences.isTimeToStartWorking(today)) {
                    statusAction = WorkerStatusAction.CLOCKIN_WORK;
                }
                break;
            case WORK_BEFORE_LUNCH:
                if (currentPreferences.isTimeToStartLunch(today)
                    && !workerWaitService.workerHasAllDayWaitForAction(currentPreferences.getWorkerInternalId(), WorkerStatusAction.CLOCKIN_LUNCH, timestamp)) {
                    statusAction = WorkerStatusAction.CLOCKIN_LUNCH;
                } else if (currentPreferences.isTimeToEndWorking(today)) {
                    statusAction = WorkerStatusAction.CLOCKOUT_WORK;
                }
                break;
            case WORK_AFTER_LUNCH:
                if (currentPreferences.isTimeToEndWorking(today)) {
                    statusAction = WorkerStatusAction.CLOCKOUT_WORK;
                }
                break;
            case LUNCH:
                if (currentPreferences.isTimeToEndLunch(today)) {
                    statusAction = WorkerStatusAction.CLOCKOUT_LUNCH;
                }
                break;
        }

        return Uni.createFrom().item(statusAction);
    }
}