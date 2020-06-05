package com.kronostools.timehammer.core.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.ChatbotMessages;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessage;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessageBuilder;
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

import static com.kronostools.timehammer.common.utils.CommonUtils.stringFormat;

@ApplicationScoped
public class RegistrationRequestNotifierProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationRequestNotifierProcessor.class);

    private final Emitter<TelegramChatbotNotificationMessage> registrationRequestNotifierChannel;

    public RegistrationRequestNotifierProcessor(@Channel(Channels.NOTIFICATION_TELEGRAM) final Emitter<TelegramChatbotNotificationMessage> registrationRequestNotifierChannel) {
        this.registrationRequestNotifierChannel = registrationRequestNotifierChannel;
    }

    @Incoming(Channels.WORKER_REGISTER_NOTIFY_IN)
    public CompletionStage<Void> routeRegistrationRequest(final Message<WorkerRegistrationRequestMessage> message) {
        final WorkerRegistrationRequestMessage registrationRequest = WorkerRegistrationRequestMessageBuilder.copy(message.getPayload()).build();

        if (registrationRequest.getSaveWorkerPhase().isNotSuccessful()) {
            LOG.warn("Unsuccessful registration request, nothing to notify");
            return message.ack();
        } else {
            LOG.info("Notifying registration request message '{}' to worker ...", registrationRequest.getRegistrationRequestId());

            final TelegramChatbotNotificationMessage notificationMessage = new TelegramChatbotNotificationMessageBuilder()
                    .generated(registrationRequest.getGenerated())
                    .chatId(registrationRequest.getCheckRegistrationRequestPhase().getChatId())
                    .text(ChatbotMessages.SUCCESSFUL_REGISTRATION)
                    .build();

            return registrationRequestNotifierChannel.send(notificationMessage)
                    .handle((Void, e) -> {
                        if (e != null) {
                            LOG.error(stringFormat("Exception while notifying registration request message '{}'", registrationRequest.getRegistrationRequestId()), e);
                        } else {
                            message.ack();
                            LOG.debug("Notified registration request message '{}'", registrationRequest.getRegistrationRequestId());
                        }

                        return null;
                    });
        }
    }
}