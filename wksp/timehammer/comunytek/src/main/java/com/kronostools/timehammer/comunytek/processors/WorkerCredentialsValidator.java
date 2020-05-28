package com.kronostools.timehammer.comunytek.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.WorkerCredentialsResult;
import com.kronostools.timehammer.common.messages.registration.CheckWorkerCredentialsPhase;
import com.kronostools.timehammer.common.messages.registration.CheckWorkerCredentialsPhaseBuilder;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequest;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestBuilder;
import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.constants.ComunytekLoginResult;
import com.kronostools.timehammer.comunytek.model.ComunytekLoginResponseBuilder;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkerCredentialsValidator {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerCredentialsValidator.class);

    private final ComunytekClient comunytekClient;

    public WorkerCredentialsValidator(final ComunytekClient comunytekClient) {
        this.comunytekClient = comunytekClient;
    }

    @Incoming(Channels.COMUNYTEK_WORKER_REGISTER)
    @Outgoing(Channels.WORKER_REGISTER_VALIDATE)
    public Uni<Message<WorkerRegistrationRequest>> checkCredentials(final Message<WorkerRegistrationRequest> message) {
        final WorkerRegistrationRequest registrationRequest = WorkerRegistrationRequestBuilder.copy(message.getPayload()).build();

        return comunytekClient.login(registrationRequest.getWorkerExternalId(), registrationRequest.getWorkerExternalPassword())
                .onFailure(Exception.class)
                    .recoverWithItem((e) -> new ComunytekLoginResponseBuilder()
                            .result(ComunytekLoginResult.UNEXPECTED_ERROR)
                            .errorMessage(e.getMessage())
                            .build())
                .flatMap(comunytekLoginResponse -> {
                    final CheckWorkerCredentialsPhase result;

                    switch (comunytekLoginResponse.getResult()) {
                        case OK:
                            result = new CheckWorkerCredentialsPhaseBuilder()
                                    .result(WorkerCredentialsResult.OK)
                                    .build();
                            break;
                        case INVALID:
                            result = new CheckWorkerCredentialsPhaseBuilder()
                                    .result(WorkerCredentialsResult.INVALID)
                                    .build();
                            break;
                        default:
                            result = new CheckWorkerCredentialsPhaseBuilder()
                                    .result(WorkerCredentialsResult.UNEXPECTED_ERROR)
                                    .build();
                            break;
                    }

                    registrationRequest.setCheckWorkerCredentialsPhase(result);

                    return Uni.createFrom().item(Message.of(registrationRequest, message::ack));
                });
    }
}