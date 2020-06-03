package com.kronostools.timehammer.core.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.SimpleValidateResult;
import com.kronostools.timehammer.common.messages.registration.ValidateRegistrationRequestPhase;
import com.kronostools.timehammer.common.messages.registration.ValidateRegistrationRequestPhaseBuilder;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequest;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestBuilder;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegistrationRequestValidatorProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationRequestValidatorProcessor.class);

    @Incoming(Channels.WORKER_REGISTER_VALIDATE)
    @Outgoing(Channels.WORKER_REGISTER_PERSIST_OUT)
    public Uni<Message<WorkerRegistrationRequest>> process(final Message<WorkerRegistrationRequest> message) {
        final WorkerRegistrationRequest registrationRequest = WorkerRegistrationRequestBuilder.copy(message.getPayload()).build();

        LOG.info("Validating registration request '{}' ...", registrationRequest.getRegistrationRequestForm().getWorkerInternalId());

        // TODO: implement real validations
        final ValidateRegistrationRequestPhase validationResult = new ValidateRegistrationRequestPhaseBuilder()
                .result(SimpleValidateResult.VALID)
                //TODO: fill validatedForm and validationErrors
                .build();

        registrationRequest.setValidateRegistrationRequestPhase(validationResult);

        LOG.info("Validated registration request '{}' with result: {}", registrationRequest.getRegistrationRequestForm().getWorkerExternalId(), validationResult.getResult().name());

        return Uni.createFrom().item(Message.of(registrationRequest, message::ack));
    }
}