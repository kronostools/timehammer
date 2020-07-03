package com.kronostools.timehammer.statemachine.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.constants.WorkerStatusAction;
import com.kronostools.timehammer.common.messages.constants.AnswerOption;
import com.kronostools.timehammer.common.messages.constants.ChatbotMessages;
import com.kronostools.timehammer.common.messages.constants.WorkerStatusResult;
import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorker;
import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorkerBuilder;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessage;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessageBuilder;
import com.kronostools.timehammer.common.messages.telegramchatbot.model.KeyboardOption;
import com.kronostools.timehammer.common.messages.telegramchatbot.model.KeyboardOptionBuilder;
import com.kronostools.timehammer.statemachine.constants.QuestionType;
import com.kronostools.timehammer.statemachine.service.WorkerWaitService;
import com.kronostools.timehammer.statemachine.utils.AnswerUtils;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class WorkerStatusNotifierProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerStatusNotifierProcessor.class);

    private final WorkerWaitService workerWaitService;

    public WorkerStatusNotifierProcessor(final WorkerWaitService workerWaitService) {
        this.workerWaitService = workerWaitService;
    }

    @Incoming(Channels.STATUS_WORKER_NOTIFY)
    @Outgoing(Channels.NOTIFICATION_TELEGRAM)
    public Multi<Message<TelegramChatbotNotificationMessage>> routeWorkerStatus(final Message<CheckWorkersStatusWorker> message) {
        final CheckWorkersStatusWorker worker = CheckWorkersStatusWorkerBuilder.copy(message.getPayload()).build();

        LOG.info("Determining status notification for worker '{}' ...", worker.getWorkerCurrentPreferences().getWorkerInternalId());

        String notificationText = null;
        List<KeyboardOption> notificationKeyboard = null;

        if (worker.getWorkerStatusPhase().getResult() == WorkerStatusResult.MISSING_OR_INVALID_CREDENTIALS) {
            notificationText = ChatbotMessages.MISSING_PASSWORD;
        } else if (worker.getWorkerStatusActionPhase() == null
                || worker.getWorkerStatusActionPhase().isNotSuccessful()) {
            LOG.warn("Unsuccessful status check for worker '{}', nothing to notify", worker.getWorkerCurrentPreferences().getWorkerInternalId());
        } else if (worker.getWorkerStatusActionPhase().getWorkerStatusAction() == WorkerStatusAction.NOOP) {
            LOG.warn("Status action is '{}' for worker '{}', nothing to notify", WorkerStatusAction.NOOP, worker.getWorkerCurrentPreferences().getWorkerInternalId());
        } else {
            if (worker.getWorkerStatusActionPhase().getWorkerStatusAction() != WorkerStatusAction.NOOP) {
                final Optional<QuestionType> questionType = determineQuestion(worker.getWorkerStatusActionPhase().getWorkerStatusAction());

                if (questionType.isPresent()) {
                    if (workerWaitService.workerHasWaitForQuestionAt(worker.getWorkerCurrentPreferences().getWorkerInternalId(), questionType.get(), worker.getGenerated())) {
                        LOG.info("Worker '{}' should be asked '{}', but there is a current wait", worker.getWorkerCurrentPreferences().getWorkerInternalId(), questionType.get().name());
                    } else {
                        LOG.info("Status context is '{}' and status action is '{}', notifying question '{}' to worker '{}' ...",
                                worker.getWorkerStatusPhase().getStatusContext().name(),
                                worker.getWorkerStatusActionPhase().getWorkerStatusAction().name(),
                                questionType.get().name(),
                                worker.getWorkerCurrentPreferences().getWorkerInternalId());

                        notificationText = questionType.get().getText();
                        notificationKeyboard = getKeyboardOptions(questionType.get(), worker.getWorkerCurrentPreferences().getCompany());
                    }
                } else {
                    LOG.warn("No question could be determined for action '{}'", worker.getWorkerStatusActionPhase().getWorkerStatusAction().name());
                }
            } else {
                LOG.info("Status action is '{}', nothing to notify to worker '{}'",
                        worker.getWorkerStatusActionPhase().getWorkerStatusAction().name(),
                        worker.getWorkerCurrentPreferences().getWorkerInternalId());
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

    private Optional<QuestionType> determineQuestion(final WorkerStatusAction action) {
        final QuestionType questionType;

        switch (action) {
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

        return Optional.ofNullable(questionType);
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

    private List<KeyboardOption> getKeyboardOptions(final QuestionType questionType, final Company company) {
        return AnswerOption.getAnswerOptions(questionType.getContext(), questionType.getContextAction()).stream()
                .map(ao -> new KeyboardOptionBuilder()
                        .code(AnswerUtils.getAnswerCode(ao, company))
                        .text(ao.getButtonText())
                        .build())
                .collect(Collectors.toList());
    }
}