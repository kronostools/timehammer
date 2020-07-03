package com.kronostools.timehammer.statemachine.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.ChatbotMessages;
import com.kronostools.timehammer.common.messages.constants.WorkerStatusResult;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotAnswerMessage;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotAnswerMessageBuilder;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessage;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotNotificationMessageBuilder;
import com.kronostools.timehammer.statemachine.service.WorkerWaitService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AnswerNotifierProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(AnswerNotifierProcessor.class);

    private final WorkerWaitService workerWaitService;

    public AnswerNotifierProcessor(final WorkerWaitService workerWaitService) {
        this.workerWaitService = workerWaitService;
    }

    @Incoming(Channels.ANSWER_NOTIFY)
    @Outgoing(Channels.ANSWER_NOTIFICATION_TELEGRAM)
    public Uni<Message<TelegramChatbotNotificationMessage>> answerNotify(final Message<TelegramChatbotAnswerMessage> message) {
        final TelegramChatbotAnswerMessage answerMessage = TelegramChatbotAnswerMessageBuilder.copy(message.getPayload()).build();

        LOG.info("Determining answer notification for worker '{}' ...", answerMessage.getWorkerCurrentPreferencesPhase().getWorkerInternalId());

        String notificationText = null;

        if (answerMessage.isWait()) {
            notificationText = answerMessage.getAnswerOption().getResponseTextSuccess();
        } else if (answerMessage.getUpdateWorkerStatusPhase().isNotSuccessful()) {
            if (answerMessage.getUpdateWorkerStatusPhase().getResult() == WorkerStatusResult.MISSING_OR_INVALID_CREDENTIALS) {
                notificationText = ChatbotMessages.MISSING_PASSWORD;
            } else {
                notificationText = answerMessage.getAnswerOption().getResponseTextError();
            }
        } else {
            notificationText = answerMessage.getAnswerOption().getResponseTextSuccess();
        }

        return Uni.createFrom().item(Message.of(new TelegramChatbotNotificationMessageBuilder()
                .generated(answerMessage.getGenerated())
                .chatId(answerMessage.getChatId())
                .text(notificationText)
                .clearPreviousKeyboard(true)
                .build(), message::ack));
    }
}