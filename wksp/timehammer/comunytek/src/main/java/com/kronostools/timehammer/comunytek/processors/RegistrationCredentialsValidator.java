package com.kronostools.timehammer.comunytek.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.WorkerCredentialsResult;
import com.kronostools.timehammer.common.messages.registration.CheckWorkerCredentialsPhase;
import com.kronostools.timehammer.common.messages.registration.CheckWorkerCredentialsPhaseBuilder;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessage;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessageBuilder;
import com.kronostools.timehammer.comunytek.service.WorkerCredentialValidatorService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegistrationCredentialsValidator {
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationCredentialsValidator.class);

    private final WorkerCredentialValidatorService workerCredentialValidatorService;

    public RegistrationCredentialsValidator(final WorkerCredentialValidatorService workerCredentialValidatorService) {
        this.workerCredentialValidatorService = workerCredentialValidatorService;
    }

    @Incoming(Channels.COMUNYTEK_WORKER_REGISTER)
    @Outgoing(Channels.WORKER_REGISTER_PERSIST)
    public Uni<Message<WorkerRegistrationRequestMessage>> checkCredentials(final Message<WorkerRegistrationRequestMessage> message) {
        final WorkerRegistrationRequestMessage registrationRequest = WorkerRegistrationRequestMessageBuilder.copy(message.getPayload()).build();

        LOG.info("Checking credentials of worker '{}' against Comunytek ...", registrationRequest.getRegistrationRequestForm().getWorkerExternalId());

        return workerCredentialValidatorService.checkWorkerCredentials(registrationRequest.getRegistrationRequestForm().getWorkerExternalId(), registrationRequest.getRegistrationRequestForm().getWorkerExternalPassword())
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