package com.kronostools.timehammer.service;

import com.kronostools.timehammer.chatbot.service.NotificationService;
import com.kronostools.timehammer.comunytek.dto.ComunytekStatusDto;
import com.kronostools.timehammer.enums.QuestionType;
import com.kronostools.timehammer.enums.WorkerStatusEventType;
import com.kronostools.timehammer.manager.WorkerChatManager;
import com.kronostools.timehammer.vo.WorkerCurrentPreferencesVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.Set;

@ApplicationScoped
public class WorkerStatusService {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerStatusService.class);

    private final WorkerChatManager workerChatManager;
    private final NotificationService notificationService;

    public WorkerStatusService(final WorkerChatManager workerChatManager,
                               final NotificationService notificationService) {
        this.workerChatManager = workerChatManager;
        this.notificationService = notificationService;
    }

    public void processStatusEvent(final LocalDateTime timestamp, final WorkerStatusEventType workerStatusEvent, final ComunytekStatusDto comunytekStatusDto, final WorkerCurrentPreferencesVo workerCurrentPreferences) {
        LOG.debug("BEGIN processStatusEvent: [{}] [{}]", workerStatusEvent, comunytekStatusDto);

        // TODO: try to implement a Rule pattern to avoid the excessive use of if-else (see https://www.baeldung.com/java-replace-if-statements)
        switch (workerStatusEvent) {
            case TICK:
                final Set<String> workerChats = workerChatManager.getWorkerChatsByInternalId(workerCurrentPreferences.getWorkerInternalId());

                switch (comunytekStatusDto.getStatus()) {
                    case INITIAL:
                        if (workerCurrentPreferences.isTimeToStartWorking(timestamp.toLocalTime())) {
                            notificationService.question(workerChats, QuestionType.START, timestamp);
                        }

                        break;
                    case STARTED:
                        if (workerCurrentPreferences.isTimeToStartLunch(timestamp.toLocalTime())) {
                            notificationService.question(workerChats, QuestionType.LUNCH_START, timestamp);
                        } else if (workerCurrentPreferences.isTimeToEndWorking(timestamp.toLocalTime())) {
                            notificationService.question(workerChats, QuestionType.END, timestamp);
                        }

                        break;
                    case PAUSED:
                        if (workerCurrentPreferences.isTimeToEndLunch(timestamp.toLocalTime())) {
                            notificationService.question(workerChats, QuestionType.LUNCH_RESUME, timestamp);
                        }

                        break;
                    case RESUMED:
                        if (workerCurrentPreferences.isTimeToEndWorking(timestamp.toLocalTime())) {
                            notificationService.question(workerChats, QuestionType.END, timestamp);
                        }

                        break;
                    default:
                        LOG.debug("Nothing to do with event {} when worker status is {}", workerStatusEvent, comunytekStatusDto.getStatus());

                        break;
                }

                break;
            default:
                LOG.warn("Business logic not implemented for event {}", workerStatusEvent);

                break;
        }

        LOG.debug("END processStatusEvent");
    }
}