package com.kronostools.timehammer.core.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.SaveWorkerResult;
import com.kronostools.timehammer.common.messages.constants.WorkerCredentialsResult;
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
    @Outgoing(Channels.WORKER_REGISTER_NOTIFY_OUT)
    public Uni<Message<WorkerRegistrationRequestMessage>> process(final Message<WorkerRegistrationRequestMessage> message) {
        final WorkerRegistrationRequestMessage registrationRequest = WorkerRegistrationRequestMessageBuilder.copy(message.getPayload()).build();

        LOG.info("Trying to save new worker '{}' ...", registrationRequest.getRegistrationRequestId());

        if (registrationRequest.getCheckWorkerCredentialsPhase().isSuccessful()) {
            return workerService.saveWorker(registrationRequest)
                    .onFailure()
                    .recoverWithItem(new SaveWorkerPhaseBuilder()
                            .result(SaveWorkerResult.KO)
                            .errorMessage("There was an error while saving worker")
                            .build())
                    .flatMap(swp -> Uni.createFrom()
                            .item(Message.of(WorkerRegistrationRequestMessageBuilder
                                    .copy(registrationRequest)
                                    .saveWorkerPhase(swp)
                                    .build(), message::ack)));
        } else {
            if (registrationRequest.getCheckWorkerCredentialsPhase().getResult() == WorkerCredentialsResult.INVALID) {
                return Uni.createFrom().item(Message.of(WorkerRegistrationRequestMessageBuilder
                        .copy(registrationRequest)
                        .saveWorkerPhase(new SaveWorkerPhaseBuilder()
                                .result(SaveWorkerResult.INVALID_CREDENTIALS)
                                .errorMessage("No worker was saved because its credentials are invalid")
                                .build())
                        .build(), message::ack));
            } else {
                return Uni.createFrom().item(Message.of(WorkerRegistrationRequestMessageBuilder
                        .copy(registrationRequest)
                        .saveWorkerPhase(new SaveWorkerPhaseBuilder()
                                .result(SaveWorkerResult.KO)
                                .errorMessage("No worker was saved because its credentials are invalid")
                                .build())
                        .build(), message::ack));
            }
        }
    }
}