package com.kronostools.timehammer.integration.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotAnswerMessage;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotAnswerMessageBuilder;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

import static com.kronostools.timehammer.common.utils.CommonUtils.stringFormat;

@ApplicationScoped
public class AnswerRouter {
    private static final Logger LOG = LoggerFactory.getLogger(AnswerRouter.class);

    private final Emitter<TelegramChatbotAnswerMessage> comunytekAnswerMessageChannel;

    public AnswerRouter(@Channel(Channels.COMUNYTEK_ANSWER_EXECUTE) final Emitter<TelegramChatbotAnswerMessage> comunytekAnswerMessageChannel) {
        this.comunytekAnswerMessageChannel = comunytekAnswerMessageChannel;
    }

    @Incoming(Channels.ANSWER_EXECUTE)
    public CompletionStage<Void> routeHolidays(final Message<TelegramChatbotAnswerMessage> message) {
        final TelegramChatbotAnswerMessage answerMessage = TelegramChatbotAnswerMessageBuilder.copy(message.getPayload()).build();

        LOG.info("Routing answer message of worker '{}' to company '{}'", answerMessage.getWorkerCurrentPreferencesPhase().getWorkerInternalId(), answerMessage.getCompany().getCode());

        if (answerMessage.getCompany() == Company.COMUNYTEK) {
            return comunytekAnswerMessageChannel.send(answerMessage).handle(getMessageHandler(message, answerMessage.getWorkerCurrentPreferencesPhase().getCompany()));
        } else {
            LOG.warn("Ignored answer message because there is no channel for company '{}'", answerMessage.getWorkerCurrentPreferencesPhase().getCompany().getCode());
            return message.ack();
        }
    }

    private BiFunction<? super Void, Throwable, Void> getMessageHandler(final Message<?> message, final Company company) {
        return (Void, e) -> {
            if (e != null) {
                LOG.error(stringFormat("Exception while routing answer message to '{}' channel", company.getCode()), e);
            } else {
                message.ack();
                LOG.debug("Routed answer message to '{}' channel", company.getCode());
            }

            return null;
        };
    }
}