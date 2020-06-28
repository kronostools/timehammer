package com.kronostools.timehammer.commandprocessor.processors;

import com.kronostools.timehammer.commandprocessor.model.ChatbotUpdatePasswordRequest;
import com.kronostools.timehammer.commandprocessor.service.UpdatePasswordRequestService;
import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;
import com.kronostools.timehammer.common.messages.updatePassword.CheckUpdatePasswordRequestPhaseBuilder;
import com.kronostools.timehammer.common.messages.updatePassword.WorkerUpdatePasswordRequestMessage;
import com.kronostools.timehammer.common.messages.updatePassword.WorkerUpdatePasswordRequestMessageBuilder;
import com.kronostools.timehammer.common.utils.CommonUtils;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UpdatePasswordAliveChecker {
    private static final Logger LOG = LoggerFactory.getLogger(UpdatePasswordAliveChecker.class);

    private final UpdatePasswordRequestService updatePasswordRequestService;

    public UpdatePasswordAliveChecker(final UpdatePasswordRequestService updatePasswordRequestService) {
        this.updatePasswordRequestService = updatePasswordRequestService;
    }

    @Incoming(Channels.WORKER_UPDATE_PASSWORD_INIT)
    @Outgoing(Channels.WORKER_UPDATE_PASSWORD_VALIDATE)
    public Uni<Message<WorkerUpdatePasswordRequestMessage>> process(final Message<WorkerUpdatePasswordRequestMessage> message) {
        final WorkerUpdatePasswordRequestMessage inputMessage = WorkerUpdatePasswordRequestMessageBuilder.copy(message.getPayload()).build();

        LOG.info("Checking if update password request '{}' is still alive ...", inputMessage.getRequestId());

        final ChatbotUpdatePasswordRequest existingUpdatePasswordRequest = updatePasswordRequestService.getUpdatePasswordRequest(inputMessage.getRequestId());

        if (existingUpdatePasswordRequest == null) {
            final String errorMessage = CommonUtils.stringFormat("Update password request '{}' is already expired", inputMessage.getRequestId());

            LOG.warn(errorMessage);

            inputMessage.setCheckUpdatePasswordRequestPhase(new CheckUpdatePasswordRequestPhaseBuilder()
                    .result(SimpleResult.KO)
                    .errorMessage(errorMessage)
                    .build());
        } else {
            LOG.debug("Update password request '{}' was found and is associated to chat '{}'", inputMessage.getRequestId(), existingUpdatePasswordRequest.getChatId());

            inputMessage.setCheckUpdatePasswordRequestPhase(new CheckUpdatePasswordRequestPhaseBuilder()
                    .result(SimpleResult.OK)
                    .workerInternalId(existingUpdatePasswordRequest.getWorkerInternalId())
                    .company(existingUpdatePasswordRequest.getCompany())
                    .workerExternalId(existingUpdatePasswordRequest.getWorkerExternalId())
                    .chatId(existingUpdatePasswordRequest.getChatId())
                    .build());
        }

        return Uni.createFrom().item(Message.of(inputMessage, message::ack));
    }
}