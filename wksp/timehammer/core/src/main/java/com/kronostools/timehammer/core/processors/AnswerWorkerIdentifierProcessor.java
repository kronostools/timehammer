package com.kronostools.timehammer.core.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.WorkerCurrentPreferencesResult;
import com.kronostools.timehammer.common.messages.schedules.model.WorkerCurrentPreferences;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotAnswerMessage;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotAnswerMessageBuilder;
import com.kronostools.timehammer.common.messages.telegramchatbot.WorkerCurrentPreferencesPhase;
import com.kronostools.timehammer.common.messages.telegramchatbot.WorkerCurrentPreferencesPhaseBuilder;
import com.kronostools.timehammer.common.services.TimeMachineService;
import com.kronostools.timehammer.common.utils.CommonUtils;
import com.kronostools.timehammer.core.dao.WorkerCurrentPreferencesDao;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AnswerWorkerIdentifierProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(AnswerWorkerIdentifierProcessor.class);

    private final WorkerCurrentPreferencesDao workerCurrentPreferencesDao;
    private final TimeMachineService timeMachineService;

    public AnswerWorkerIdentifierProcessor(final WorkerCurrentPreferencesDao workerCurrentPreferencesDao,
                                           final TimeMachineService timeMachineService) {
        this.workerCurrentPreferencesDao = workerCurrentPreferencesDao;
        this.timeMachineService = timeMachineService;
    }

    @Incoming(Channels.ANSWER_ID)
    @Outgoing(Channels.ANSWER_PROCESS)
    public Uni<Message<TelegramChatbotAnswerMessage>> process(final Message<TelegramChatbotAnswerMessage> message) {
        final TelegramChatbotAnswerMessage answerMessage = TelegramChatbotAnswerMessageBuilder.copy(message.getPayload()).build();

        LOG.info("Trying to indentify worker of answer received from chat '{}' ...", answerMessage.getChatId());

        return workerCurrentPreferencesDao
                .findByChatId(answerMessage.getChatId(), timeMachineService.getNow().toLocalDate())
                .flatMap(workerCurrentPreferencesResult -> {
                    final WorkerCurrentPreferencesPhase workerCurrentPreferencesPhase;

                    if (workerCurrentPreferencesResult.isSuccessful()) {
                        if (workerCurrentPreferencesResult.getResult() != null) {
                            final WorkerCurrentPreferences wcp = workerCurrentPreferencesResult.getResult();

                            LOG.info("Worker '{}' was identified for answer comming from chat '{}'", wcp.getWorkerExternalId(), answerMessage.getChatId());

                            workerCurrentPreferencesPhase = new WorkerCurrentPreferencesPhaseBuilder()
                                    .result(WorkerCurrentPreferencesResult.OK)
                                    .date(wcp.getDate())
                                    .workerInternalId(wcp.getWorkerInternalId())
                                    .workerExternalId(wcp.getWorkerExternalId())
                                    .workSsid(wcp.getWorkSsid())
                                    .workStart(wcp.getWorkStart())
                                    .workEnd(wcp.getWorkEnd())
                                    .lunchStart(wcp.getLunchStart())
                                    .lunchEnd(wcp.getLunchEnd())
                                    .workCityCode(wcp.getWorkCityCode())
                                    .timezone(wcp.getTimezone())
                                    .company(wcp.getCompany())
                                    .workerHoliday(wcp.isWorkerHoliday())
                                    .cityHoliday(wcp.isCityHoliday())
                                    .chatIds(wcp.getChatIds())
                                    .build();
                        } else {
                            LOG.info("No worker was identified for answer comming from chat '{}'", answerMessage.getChatId());

                            workerCurrentPreferencesPhase = new WorkerCurrentPreferencesPhaseBuilder()
                                    .result(WorkerCurrentPreferencesResult.UNREGISTERED_CHAT)
                                    .errorMessage(CommonUtils.stringFormat("There is no registered worker for chat '{}'", answerMessage.getChatId()))
                                    .build();
                        }
                    } else {
                        LOG.error("Unexpected error while identifying worker of answer from chat '{}'. Error: {}", answerMessage.getChatId(), workerCurrentPreferencesResult.getErrorMessage());

                        workerCurrentPreferencesPhase = new WorkerCurrentPreferencesPhaseBuilder()
                                .result(WorkerCurrentPreferencesResult.UNEXPECTED_ERROR)
                                .errorMessage(workerCurrentPreferencesResult.getErrorMessage())
                                .build();
                    }

                    answerMessage.setWorkerCurrentPreferencesPhase(workerCurrentPreferencesPhase);

                    return Uni.createFrom().item(Message.of(answerMessage, message::ack));
                });
    }
}