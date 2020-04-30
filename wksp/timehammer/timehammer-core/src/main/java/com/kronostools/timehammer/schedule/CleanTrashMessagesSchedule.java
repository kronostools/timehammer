package com.kronostools.timehammer.schedule;

import com.kronostools.timehammer.config.TimehammerConfig;
import com.kronostools.timehammer.config.TimehammerConfig.ScheduleName;
import com.kronostools.timehammer.service.TimeMachineService;
import com.kronostools.timehammer.service.TrashMessageService;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class CleanTrashMessagesSchedule extends Schedule {
    private final TrashMessageService trashMessageService;

    public CleanTrashMessagesSchedule(final TimehammerConfig timehammerConfig,
                                      final TimeMachineService timeMachineService,
                                      final TrashMessageService trashMessageService) {
        super(timehammerConfig, timeMachineService);
        this.trashMessageService = trashMessageService;
    }

    @Scheduled(cron = "{timehammer.schedules.cleanTrashMessages.cron}")
    void cleanTrashMessages() {
        run();
    }

    @Override
    protected void mainLogic(LocalDateTime timestamp) {
        trashMessageService.cleanTrashMessages();
    }

    @Override
    protected ScheduleName getScheduleName() {
        return ScheduleName.CLEAN_TRASH_MESSAGES;
    }
}