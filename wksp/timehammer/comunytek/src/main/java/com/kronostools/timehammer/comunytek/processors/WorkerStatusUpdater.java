package com.kronostools.timehammer.comunytek.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.WorkerStatusResult;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkerStatusPhase;
import com.kronostools.timehammer.common.messages.schedules.UpdateWorkerStatusPhaseBuilder;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotAnswerMessage;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotAnswerMessageBuilder;
import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.constants.ComunytekAction;
import com.kronostools.timehammer.comunytek.constants.ComunytekStatusResult;
import com.kronostools.timehammer.comunytek.model.ComunytekUpdateStatusResponseBuilder;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkerStatusUpdater {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerStatusUpdater.class);

    private final ComunytekClient comunytekClient;

    public WorkerStatusUpdater(final ComunytekClient comunytekClient) {
        this.comunytekClient = comunytekClient;
    }

    @Incoming(Channels.COMUNYTEK_ANSWER_EXECUTE)
    @Outgoing(Channels.ANSWER_NOTIFY)
    public Uni<Message<TelegramChatbotAnswerMessage>> retrieveStatus(final Message<TelegramChatbotAnswerMessage> message) {
        final TelegramChatbotAnswerMessage answerMessage = TelegramChatbotAnswerMessageBuilder.copy(message.getPayload()).build();

        LOG.info("Updating status of worker '{}' in Comunytek ...", answerMessage.getWorkerCurrentPreferencesPhase().getWorkerInternalId());

        return comunytekClient
                .updateStatus(answerMessage.getWorkerCurrentPreferencesPhase().getWorkerExternalId(), ComunytekAction.fromAnswerOption(answerMessage.getAnswerOption()), answerMessage.getGenerated())
                .onFailure(Exception.class)
                    .recoverWithItem((e) -> new ComunytekUpdateStatusResponseBuilder()
                            .result(ComunytekStatusResult.KO)
                            .errorMessage(e.getMessage())
                            .build())
                .map(statusResponse -> {
                    final UpdateWorkerStatusPhase updateWorkerStatusPhase;

                    if (statusResponse.isSuccessful()) {
                        LOG.info("Updated status of worker '{}'", answerMessage.getWorkerCurrentPreferencesPhase().getWorkerInternalId());

                        updateWorkerStatusPhase = new UpdateWorkerStatusPhaseBuilder()
                                .result(WorkerStatusResult.OK)
                                .build();
                    } else {
                        if (statusResponse.getResult() == ComunytekStatusResult.MISSING_OR_INVALID_CREDENTIALS) {
                            updateWorkerStatusPhase = new UpdateWorkerStatusPhaseBuilder()
                                    .result(WorkerStatusResult.MISSING_OR_INVALID_CREDENTIALS)
                                    .errorMessage(statusResponse.getErrorMessage())
                                    .build();
                        } else {
                            updateWorkerStatusPhase = new UpdateWorkerStatusPhaseBuilder()
                                    .result(WorkerStatusResult.KO)
                                    .errorMessage(statusResponse.getErrorMessage())
                                    .build();
                        }

                        LOG.warn("Status of worker '{}' couldn't be update. Reason: {}", answerMessage.getWorkerCurrentPreferencesPhase().getWorkerInternalId(), updateWorkerStatusPhase.getResult().name());
                    }

                    answerMessage.setUpdateWorkerStatusPhase(updateWorkerStatusPhase);

                    return Message.of(answerMessage, message::ack);
                });
    }
}