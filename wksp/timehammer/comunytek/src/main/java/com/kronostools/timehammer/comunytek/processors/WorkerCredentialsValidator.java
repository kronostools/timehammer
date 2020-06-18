package com.kronostools.timehammer.comunytek.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.WorkerCredentialsResult;
import com.kronostools.timehammer.common.messages.registration.CheckWorkerCredentialsPhase;
import com.kronostools.timehammer.common.messages.registration.CheckWorkerCredentialsPhaseBuilder;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessage;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessageBuilder;
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
    @Outgoing(Channels.WORKER_REGISTER_PERSIST)
    public Uni<Message<WorkerRegistrationRequestMessage>> checkCredentials(final Message<WorkerRegistrationRequestMessage> message) {
        final WorkerRegistrationRequestMessage registrationRequest = WorkerRegistrationRequestMessageBuilder.copy(message.getPayload()).build();

        LOG.info("Checking credentials of worker '{}' against Comunytek ...", registrationRequest.getRegistrationRequestForm().getWorkerExternalId());

        return comunytekClient.login(registrationRequest.getRegistrationRequestForm().getWorkerExternalId(), registrationRequest.getRegistrationRequestForm().getWorkerExternalPassword())
                .onFailure(Exception.class)
                    .recoverWithItem((e) -> new ComunytekLoginResponseBuilder()
                            .result(ComunytekLoginResult.KO)
                            .errorMessage(e.getMessage())
                            .build())
                .flatMap(comunytekLoginResponse -> {
                    final CheckWorkerCredentialsPhase result;

                    switch (comunytekLoginResponse.getResult()) {
                        case OK:
                            result = new CheckWorkerCredentialsPhaseBuilder()
                                    .result(WorkerCredentialsResult.OK)
                                    .fullname(comunytekLoginResponse.getFullname())
                                    .build();
                            break;
                        case INVALID:
                            result = new CheckWorkerCredentialsPhaseBuilder()
                                    .result(WorkerCredentialsResult.INVALID)
                                    .build();
                            break;
                        default:
                            result = new CheckWorkerCredentialsPhaseBuilder()
                                    .result(WorkerCredentialsResult.KO)
                                    .build();
                            break;
                    }

                    registrationRequest.setCheckWorkerCredentialsPhase(result);

                    return Uni.createFrom().item(Message.of(registrationRequest, message::ack));
                });
    }
}