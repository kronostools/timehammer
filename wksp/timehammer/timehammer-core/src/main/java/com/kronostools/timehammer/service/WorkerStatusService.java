package com.kronostools.timehammer.service;

import com.kronostools.timehammer.chatbot.service.NotificationService;
import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.dto.ComunytekStatusDto;
import com.kronostools.timehammer.comunytek.enums.ComunytekStatusValue;
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
    private final ComunytekClient comunytekClient;

    public WorkerStatusService(final WorkerChatManager workerChatManager,
                               final NotificationService notificationService,
                               final ComunytekClient comunytekClient) {
        this.workerChatManager = workerChatManager;
        this.notificationService = notificationService;
        this.comunytekClient = comunytekClient;
    }

    public void processStatusEvent(final LocalDateTime timestamp, final WorkerStatusEventType workerStatusEvent, final WorkerCurrentPreferencesVo workerCurrentPreferences, final String externalPassword) {
        LOG.debug("BEGIN processStatusEvent: [{}] [{}]", workerStatusEvent, workerCurrentPreferences);

        // TODO: try to implement a Rule pattern to avoid the excessive use of if-else (see https://www.baeldung.com/java-replace-if-statements)
        if (externalPassword != null) {
            final ComunytekStatusDto workerCurrentStatus = comunytekClient.getStatus(workerCurrentPreferences.getWorkerExternalId(), externalPassword, timestamp);

            if (workerCurrentStatus.getStatus() == ComunytekStatusValue.UNKNOWN) {
                LOG.warn("Status of worker could not be updated because the status obtained is UNKNOWN");
            } else {
                if (workerCurrentStatus.getStatus() == ComunytekStatusValue.ENDED) {
                    LOG.info("Worker has already ended working, nothing to process");
                } else {
                    switch (workerStatusEvent) {
                        case TICK:
                            QuestionType question = null;

                            switch (workerCurrentStatus.getStatus()) {
                                case INITIAL:
                                    if (workerCurrentPreferences.isTimeToStartWorking(timestamp.toLocalTime())) {
                                        question = QuestionType.START;
                                    }

                                    break;
                                case STARTED:
                                    if (workerCurrentPreferences.isTimeToStartLunch(timestamp.toLocalTime())) {
                                        question = QuestionType.LUNCH_START;
                                    } else if (workerCurrentPreferences.isTimeToEndWorking(timestamp.toLocalTime())) {
                                        question = QuestionType.END;
                                    }

                                    break;
                                case PAUSED:
                                    if (workerCurrentPreferences.isTimeToEndLunch(timestamp.toLocalTime())) {
                                        question = QuestionType.LUNCH_RESUME;
                                    }

                                    break;
                                case RESUMED:
                                    if (workerCurrentPreferences.isTimeToEndWorking(timestamp.toLocalTime())) {
                                        question = QuestionType.END;
                                    }

                                    break;
                                default:
                                    LOG.debug("Nothing to do with event {} when worker status is {}", workerStatusEvent, workerCurrentStatus.getStatus());

                                    break;
                            }

                            if (question != null) {
                                final Set<String> workerChats = workerChatManager.getWorkerChatsByInternalId(workerCurrentPreferences.getWorkerInternalId());

                                notificationService.question(workerChats, question, timestamp);
                            }

                            break;
                        default:
                            LOG.warn("Business logic not implemented for event {}", workerStatusEvent);

                            break;
                    }
                }
            }
        } else {
            if (workerCurrentPreferences.canBeNotified(timestamp.toLocalTime())) {
                final Set<String> workerChats = workerChatManager.getWorkerChatsByInternalId(workerCurrentPreferences.getWorkerInternalId());

                notificationService.missingCredentials(workerChats, timestamp);
            }
        }

        LOG.debug("END processStatusEvent");
    }
}