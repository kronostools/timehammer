package com.kronostools.timehammer.core.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;
import com.kronostools.timehammer.common.messages.registration.SaveWorkerPhaseBuilder;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessage;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessageBuilder;
import com.kronostools.timehammer.core.service.WorkerService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkerSaverProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerSaverProcessor.class);

    private final WorkerService workerService;

    public WorkerSaverProcessor(final WorkerService workerService) {
        this.workerService = workerService;
    }

    @Incoming(Channels.WORKER_REGISTER_PERSIST)
    @Outgoing(Channels.WORKER_REGISTER_SAVE_NOTIFY_OUT)
    public Uni<Message<WorkerRegistrationRequestMessage>> process(final Message<WorkerRegistrationRequestMessage> message) {
        final WorkerRegistrationRequestMessage registrationRequest = WorkerRegistrationRequestMessageBuilder.copy(message.getPayload()).build();


        if (registrationRequest.getCheckWorkerCredentialsPhase().isSuccessful()) {
            LOG.info("Trying to save new worker '{}' ...", registrationRequest.getRegistrationRequestId());

            return workerService.saveWorker(registrationRequest)
                    .onFailure()
                    .recoverWithItem(new SaveWorkerPhaseBuilder()
                            .result(SimpleResult.KO)
                            .errorMessage("There was an error while saving worker")
                            .build())
                    .flatMap(swp -> Uni.createFrom()
                            .item(Message.of(WorkerRegistrationRequestMessageBuilder
                                    .copy(registrationRequest)
                                    .saveWorkerPhase(swp)
                                    .build(), message::ack)));
        } else {
            LOG.info("Nothing to save because worker credentials are invalid");

            return Uni.createFrom().item(Message.of(registrationRequest, message::ack));
        }
    }
}