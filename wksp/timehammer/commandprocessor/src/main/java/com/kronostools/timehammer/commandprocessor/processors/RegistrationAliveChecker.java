package com.kronostools.timehammer.commandprocessor.processors;

import com.kronostools.timehammer.commandprocessor.model.ChatbotRegistrationRequest;
import com.kronostools.timehammer.commandprocessor.service.RegistrationRequestService;
import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;
import com.kronostools.timehammer.common.messages.registration.CheckRegistrationRequestPhaseBuilder;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequest;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestBuilder;
import com.kronostools.timehammer.common.utils.CommonUtils;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegistrationAliveChecker {
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationAliveChecker.class);

    private final RegistrationRequestService registrationRequestService;

    public RegistrationAliveChecker(final RegistrationRequestService registrationRequestService) {
        this.registrationRequestService = registrationRequestService;
    }

    @Incoming(Channels.WORKER_REGISTER_INIT)
    @Outgoing(Channels.WORKER_REGISTER_ROUTE)
    public Uni<Message<WorkerRegistrationRequest>> process(final Message<WorkerRegistrationRequest> message) {
        final WorkerRegistrationRequest inputMessage = WorkerRegistrationRequestBuilder.copy(message.getPayload()).build();

        LOG.info("Checking if registration request '{}' is still alive ...", inputMessage.getRegistrationRequestForm().getWorkerInternalId());

        final ChatbotRegistrationRequest existingRegistrationRequest = registrationRequestService.getRegistrationRequest(inputMessage.getRegistrationRequestForm().getWorkerInternalId());

        if (existingRegistrationRequest == null) {
            final String errorMessage = CommonUtils.stringFormat("Registration request '{}' is already expired", inputMessage.getRegistrationRequestForm().getWorkerInternalId());

            LOG.warn(errorMessage);

            inputMessage.setCheckRegistrationRequestPhase(new CheckRegistrationRequestPhaseBuilder()
                    .result(SimpleResult.KO)
                    .errorMessage(errorMessage)
                    .build());
        } else {
            LOG.debug("Registration request '{}' was found and is associated to chat '{}'", inputMessage.getRegistrationRequestForm().getWorkerInternalId(), existingRegistrationRequest.getChatId());

            inputMessage.setCheckRegistrationRequestPhase(new CheckRegistrationRequestPhaseBuilder()
                    .result(SimpleResult.OK)
                    .chatId(existingRegistrationRequest.getChatId())
                    .build());
        }

        return Uni.createFrom().item(Message.of(inputMessage, message::ack));
    }
}