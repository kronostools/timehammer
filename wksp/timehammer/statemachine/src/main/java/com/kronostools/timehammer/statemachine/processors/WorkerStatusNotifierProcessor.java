package com.kronostools.timehammer.statemachine.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.WorkerStatusAction;
import com.kronostools.timehammer.common.messages.constants.ChatbotMessages;
import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorker;
import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorkerBuilder;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessage;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessageBuilder;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class WorkerStatusNotifierProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerStatusNotifierProcessor.class);

    private final Emitter<TelegramChatbotNotificationMessage> registrationRequestNotifierChannel;

    public WorkerStatusNotifierProcessor(@Channel(Channels.NOTIFICATION_TELEGRAM) final Emitter<TelegramChatbotNotificationMessage> registrationRequestNotifierChannel) {
        this.registrationRequestNotifierChannel = registrationRequestNotifierChannel;
    }

    @Incoming(Channels.STATUS_WORKER_NOTIFY)
    public CompletionStage<Void> routeRegistrationRequest(final Message<CheckWorkersStatusWorker> message) {
        final CheckWorkersStatusWorker worker = CheckWorkersStatusWorkerBuilder.copy(message.getPayload()).build();

        if (worker.getWorkerStatusActionPhase() == null
                || worker.getWorkerStatusActionPhase().isNotSuccessful()) {
            LOG.warn("Unsuccessful status check, nothing to notify");
            return message.ack();
        } if (worker.getWorkerStatusActionPhase().getWorkerStatusAction() == WorkerStatusAction.NOOP) {
            LOG.warn("Status action is '{}', nothing to notify", WorkerStatusAction.NOOP);
            return message.ack();
        } else {
            LOG.info("Status context is '{}' and status action is '{}' for worker '{}', notifying it ...",
                    worker.getWorkerStatusPhase().getStatusContext().name(),
                    worker.getWorkerStatusActionPhase().getWorkerStatusAction().name(),
                    worker.getWorkerCurrentPreferences().getWorkerInternalId());

            final TelegramChatbotNotificationMessage notificationMessage = new TelegramChatbotNotificationMessageBuilder()
                    .generated(worker.getGenerated())
                    .chatId(worker.getCheckRegistrationRequestPhase().getChatId())
                    .text(ChatbotMessages.SUCCESSFUL_REGISTRATION)
                    .build();

            return registrationRequestNotifierChannel.send(notificationMessage)
                    .handle((Void, e) -> {
                        if (e != null) {
                            LOG.error(stringFormat("Exception while notifying registration request message '{}'", worker.getRegistrationRequestId()), e);
                        } else {
                            message.ack();
                            LOG.debug("Notified registration request message '{}'", worker.getRegistrationRequestId());
                        }

                        return null;
                    });
        }
    }
}