package com.kronostools.timehammer.core.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.ChatbotMessages;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessage;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessageBuilder;
import com.kronostools.timehammer.common.messages.updatePassword.WorkerUpdatePasswordRequestMessage;
import com.kronostools.timehammer.common.messages.updatePassword.WorkerUpdatePasswordRequestMessageBuilder;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;

import static com.kronostools.timehammer.common.utils.CommonUtils.stringFormat;

@ApplicationScoped
public class UpdatePasswordRequestNotifierProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(UpdatePasswordRequestNotifierProcessor.class);

    private final Emitter<TelegramChatbotNotificationMessage> updatePasswordRequestNotifierChannel;

    public UpdatePasswordRequestNotifierProcessor(@Channel(Channels.NOTIFICATION_TELEGRAM) final Emitter<TelegramChatbotNotificationMessage> updatePasswordRequestNotifierChannel) {
        this.updatePasswordRequestNotifierChannel = updatePasswordRequestNotifierChannel;
    }

    @Incoming(Channels.WORKER_UPDATE_PASSWORD_NOTIFY)
    public CompletionStage<Void> routeUpdatePasswordRequest(final Message<WorkerUpdatePasswordRequestMessage> message) {
        final WorkerUpdatePasswordRequestMessage updatePasswordRequest = WorkerUpdatePasswordRequestMessageBuilder.copy(message.getPayload()).build();

        if (updatePasswordRequest.getCheckUpdatePasswordCredentialsPhase() == null
                || updatePasswordRequest.getCheckUpdatePasswordCredentialsPhase().isNotSuccessful()) {
            LOG.warn("Unsuccessful update password request, nothing to notify");
            return message.ack();
        } else {
            LOG.info("Notifying update password request message '{}' to worker ...", updatePasswordRequest.getRequestId());

            final TelegramChatbotNotificationMessage notificationMessage = new TelegramChatbotNotificationMessageBuilder()
                    .generated(updatePasswordRequest.getGenerated())
                    .chatId(updatePasswordRequest.getCheckUpdatePasswordRequestPhase().getChatId())
                    .text(ChatbotMessages.SUCCESSFUL_PASSWORD_UPDATE)
                    .build();

            return updatePasswordRequestNotifierChannel.send(notificationMessage)
                    .handle((Void, e) -> {
                        if (e != null) {
                            LOG.error(stringFormat("Exception while notifying update password request message '{}'", updatePasswordRequest.getRequestId()), e);
                        } else {
                            message.ack();
                            LOG.debug("Notified update password request message '{}'", updatePasswordRequest.getRequestId());
                        }

                        return null;
                    });
        }
    }
}