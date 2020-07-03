package com.kronostools.timehammer.statemachine.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.constants.AnswerOption;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotAnswerMessage;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotAnswerMessageBuilder;
import com.kronostools.timehammer.statemachine.service.WorkerWaitService;
import com.kronostools.timehammer.statemachine.utils.AnswerUtils;
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
public class AnswerProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(AnswerProcessor.class);

    private final WorkerWaitService workerWaitService;

    private final Emitter<TelegramChatbotAnswerMessage> answerRouteChannel;
    private final Emitter<TelegramChatbotAnswerMessage> answerNotifyChannel;

    public AnswerProcessor(final WorkerWaitService workerWaitService,
                           @Channel(Channels.ANSWER_EXECUTE) final Emitter<TelegramChatbotAnswerMessage> answerRouteChannel,
                           @Channel(Channels.ANSWER_WAIT_NOTIFY) final Emitter<TelegramChatbotAnswerMessage> answerNotifyChannel) {
        this.workerWaitService = workerWaitService;

        this.answerRouteChannel = answerRouteChannel;
        this.answerNotifyChannel = answerNotifyChannel;
    }

    @Incoming(Channels.ANSWER_PROCESS)
    public CompletionStage<Void> processAnswer(final Message<TelegramChatbotAnswerMessage> message) {
        final TelegramChatbotAnswerMessage answerMessage = TelegramChatbotAnswerMessageBuilder.copy(message.getPayload()).build();

        LOG.debug("Processing answer '{}' comming from worker '{}' ...", answerMessage.getRawAnswer(), answerMessage.getWorkerCurrentPreferencesPhase().getWorkerInternalId());

        final AnswerOption answerOption = AnswerUtils.getAnswerOption(answerMessage.getRawAnswer());

        if (answerOption.isWait()) {
            workerWaitService.saveWaitForWorkerAndQuestion(answerMessage.getWorkerCurrentPreferencesPhase().getWorkerInternalId(), answerOption);

            return answerNotifyChannel.send(TelegramChatbotAnswerMessageBuilder
                    .copy(answerMessage)
                    .answerOption(answerOption)
                    .build())
                    .handle(getMessageHandler(message, answerMessage.getChatId()));
        } else {
            final Company company = AnswerUtils.getCompany(answerMessage.getRawAnswer());

            return answerRouteChannel.send(TelegramChatbotAnswerMessageBuilder
                    .copy(answerMessage)
                    .answerOption(answerOption)
                    .company(company)
                    .build())
                    .handle(getMessageHandler(message, answerMessage.getChatId()));
        }
    }

    private BiFunction<? super Void, Throwable, Void> getMessageHandler(final Message<?> message, final String chatId) {
        return (Void, e) -> {
            if (e != null) {
                LOG.error(stringFormat("Exception while routing answer from chat '{}'", chatId), e);
            } else {
                message.ack();
                LOG.debug("Registration request '{}' routed successfully", chatId);
            }

            return null;
        };
    }
}