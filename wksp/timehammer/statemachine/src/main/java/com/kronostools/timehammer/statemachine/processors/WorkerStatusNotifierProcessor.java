package com.kronostools.timehammer.statemachine.processors;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.constants.WorkerStatusAction;
import com.kronostools.timehammer.common.messages.constants.AnswerOption;
import com.kronostools.timehammer.common.messages.constants.ChatbotMessages;
import com.kronostools.timehammer.common.messages.constants.QuestionType;
import com.kronostools.timehammer.common.messages.constants.WorkerStatusResult;
import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorker;
import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorkerBuilder;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessage;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessageBuilder;
import com.kronostools.timehammer.common.messages.telegramchatbot.model.KeyboardOption;
import com.kronostools.timehammer.common.messages.telegramchatbot.model.KeyboardOptionBuilder;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.statemachine.model.Wait;
import com.kronostools.timehammer.statemachine.model.WaitId;
import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class WorkerStatusNotifierProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerStatusNotifierProcessor.class);

    private final Cache<WaitId, Wait> workerWaitsCache;

    public WorkerStatusNotifierProcessor() {
        this.workerWaitsCache = Caffeine.newBuilder().build();
    }

    @Incoming(Channels.STATUS_WORKER_NOTIFY)
    @Outgoing(Channels.NOTIFICATION_TELEGRAM)
    public Multi<Message<TelegramChatbotNotificationMessage>> routeWorkerStatus(final Message<CheckWorkersStatusWorker> message) {
        final CheckWorkersStatusWorker worker = CheckWorkersStatusWorkerBuilder.copy(message.getPayload()).build();

        String notificationText = null;
        List<KeyboardOption> notificationKeyboard = null;

        if (worker.getWorkerStatusPhase().getResult() == WorkerStatusResult.MISSING_OR_INVALID_CREDENTIALS) {
            notificationText = ChatbotMessages.MISSING_PASSWORD;
        } else if (worker.getWorkerStatusActionPhase() == null
                || worker.getWorkerStatusActionPhase().isNotSuccessful()) {
            LOG.warn("Unsuccessful status check, nothing to notify");
        } else if (worker.getWorkerStatusActionPhase().getWorkerStatusAction() == WorkerStatusAction.NOOP) {
            LOG.warn("Status action is '{}', nothing to notify", WorkerStatusAction.NOOP);
        } else {
            LOG.info("Status context is '{}' and status action is '{}' for worker '{}', notifying it ...",
                    worker.getWorkerStatusPhase().getStatusContext().name(),
                    worker.getWorkerStatusActionPhase().getWorkerStatusAction().name(),
                    worker.getWorkerCurrentPreferences().getWorkerInternalId());

            if (worker.getWorkerStatusActionPhase().getWorkerStatusAction() != WorkerStatusAction.NOOP) {
                final QuestionType questionType;

                switch (worker.getWorkerStatusActionPhase().getWorkerStatusAction()) {
                    case CLOCKIN_WORK:
                        questionType = QuestionType.QUESTION_WORK_START;
                        break;
                    case CLOCKOUT_WORK:
                        questionType = QuestionType.QUESTION_WORK_END;
                        break;
                    case CLOCKIN_LUNCH:
                        questionType = QuestionType.QUESTION_LUNCH_START;
                        break;
                    case CLOCKOUT_LUNCH:
                        questionType = QuestionType.QUESTION_LUNCH_END;
                        break;
                    default:
                        questionType = null;
                        break;
                }

                if (questionType != null) {
                    final WaitId waitId = new WaitId(worker.getWorkerCurrentPreferences().getWorkerInternalId(), questionType);

                    final Wait foundWait = workerWaitsCache.getIfPresent(waitId);

                    if (foundWait == null) {
                        notificationText = questionType.getText();
                    } else if (foundWait.isExpired(worker.getGenerated())) {
                        workerWaitsCache.invalidate(waitId);
                        notificationText = questionType.getText();
                        notificationKeyboard = getKeyboardOptions(questionType.getOptions(), worker.getWorkerCurrentPreferences().getCompany());
                    } else {
                        LOG.info("Worker '{}' should be asked '{}', but there is a current wait until '{}'", worker.getWorkerCurrentPreferences().getWorkerInternalId(), questionType.name(), CommonDateTimeUtils.formatDateTimeToLog(foundWait.getLimitTimestamp()));
                    }
                }
            }
        }

        final List<Message<TelegramChatbotNotificationMessage>> notifications;

        if (notificationText == null) {
            notifications = Collections.emptyList();
        } else {
            notifications = getNotifications(worker.getWorkerCurrentPreferences().getChatIds(), worker.getGenerated(), notificationText, notificationKeyboard);
        }

        return Multi.createFrom().iterable(notifications).on().completion(message::ack);
    }

    private List<Message<TelegramChatbotNotificationMessage>> getNotifications(final Set<String> chatIds, final LocalDateTime generated, final String text, final List<KeyboardOption> keyboardOptions) {
        return chatIds.stream()
                .map(chatId -> Message.of(new TelegramChatbotNotificationMessageBuilder()
                        .generated(generated)
                        .chatId(chatId)
                        .text(text)
                        .keyboard(keyboardOptions)
                        .build()))
                .collect(Collectors.toList());
    }

    private List<KeyboardOption> getKeyboardOptions(final List<AnswerOption> answerOptions, final Company company) {
        return answerOptions.stream()
                .map(ao -> new KeyboardOptionBuilder()
                        .answerCode(ao.getCode())
                        .company(company)
                        .text(ao.getButtonText())
                        .build())
                .collect(Collectors.toList());
    }
}