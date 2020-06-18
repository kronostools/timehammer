package com.kronostools.timehammer.core.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.WorkerCurrentPreferencesResult;
import com.kronostools.timehammer.common.messages.schedules.model.WorkerCurrentPreferences;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotInputMessage;
import com.kronostools.timehammer.common.messages.telegramchatbot.TelegramChatbotInputMessageBuilder;
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
public class CommandWorkerIdentifierProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(CommandWorkerIdentifierProcessor.class);

    private final WorkerCurrentPreferencesDao workerCurrentPreferencesDao;
    private final TimeMachineService timeMachineService;

    public CommandWorkerIdentifierProcessor(final WorkerCurrentPreferencesDao workerCurrentPreferencesDao,
                                            final TimeMachineService timeMachineService) {
        this.workerCurrentPreferencesDao = workerCurrentPreferencesDao;
        this.timeMachineService = timeMachineService;
    }

    @Incoming(Channels.COMMAND_ID_IN)
    @Outgoing(Channels.COMMAND_PROCESS)
    public Uni<Message<TelegramChatbotInputMessage>> process(final Message<TelegramChatbotInputMessage> message) {
        final TelegramChatbotInputMessage inputMessage = TelegramChatbotInputMessageBuilder.copy(message.getPayload()).build();

        LOG.info("Trying to indentify worker of message received from chat '{}' ...", inputMessage.getChatId());

        return workerCurrentPreferencesDao
                .findByChatId(inputMessage.getChatId(), timeMachineService.getNow().toLocalDate())
                .flatMap(workerCurrentPreferencesResult -> {
                    final WorkerCurrentPreferencesPhase workerCurrentPreferencesPhase;

                    if (workerCurrentPreferencesResult.isSuccessful()) {
                        if (workerCurrentPreferencesResult.getResult() != null) {
                            final WorkerCurrentPreferences wcp = workerCurrentPreferencesResult.getResult();

                            LOG.info("Worker '{}' was identified for message comming from chat '{}'", wcp.getWorkerExternalId(), inputMessage.getChatId());

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
                            LOG.info("No worker was identified for message comming from chat '{}'", inputMessage.getChatId());

                            workerCurrentPreferencesPhase = new WorkerCurrentPreferencesPhaseBuilder()
                                    .result(WorkerCurrentPreferencesResult.UNREGISTERED_CHAT)
                                    .errorMessage(CommonUtils.stringFormat("There is no registered worker for chat '{}'", inputMessage.getChatId()))
                                    .build();
                        }
                    } else {
                        LOG.error("Unexpected error while identifying worker of message from chat '{}'. Error: {}", inputMessage.getChatId(), workerCurrentPreferencesResult.getErrorMessage());

                        workerCurrentPreferencesPhase = new WorkerCurrentPreferencesPhaseBuilder()
                                .result(WorkerCurrentPreferencesResult.UNEXPECTED_ERROR)
                                .errorMessage(workerCurrentPreferencesResult.getErrorMessage())
                                .build();
                    }

                    inputMessage.setWorkerCurrentPreferencesPhase(workerCurrentPreferencesPhase);

                    return Uni.createFrom().item(Message.of(inputMessage, message::ack));
                });
    }
}