package com.kronostools.timehammer.comunytek.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.WorkerStatusResult;
import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorker;
import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorkerBuilder;
import com.kronostools.timehammer.common.messages.schedules.GetWorkerStatusPhase;
import com.kronostools.timehammer.common.messages.schedules.GetWorkerStatusPhaseBuilder;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.constants.ComunytekStatusResult;
import com.kronostools.timehammer.comunytek.model.ComunytekStatusResponseBuilder;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkerStatusRetriever {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerStatusRetriever.class);

    private final ComunytekClient comunytekClient;

    public WorkerStatusRetriever(final ComunytekClient comunytekClient) {
        this.comunytekClient = comunytekClient;
    }

    @Incoming(Channels.COMUNYTEK_WORKER_STATUS)
    @Outgoing(Channels.STATUS_WORKER_PROCESS)
    public Uni<Message<CheckWorkersStatusWorker>> retrieveStatus(final Message<CheckWorkersStatusWorker> message) {
        final CheckWorkersStatusWorker checkWorkersStatusWorker = CheckWorkersStatusWorkerBuilder.copy(message.getPayload()).build();

        LOG.info("Getting status of worker '{}' from Comunytek ...", checkWorkersStatusWorker.getWorkerCurrentPreferences().getWorkerInternalId());

        return comunytekClient
                .getStatus(checkWorkersStatusWorker.getWorkerCurrentPreferences().getWorkerExternalId(), checkWorkersStatusWorker.getGenerated())
                .onFailure(Exception.class)
                    .recoverWithItem((e) -> new ComunytekStatusResponseBuilder()
                            .result(ComunytekStatusResult.KO)
                            .errorMessage(e.getMessage())
                            .build())
                .map(statusResponse -> {
                    final GetWorkerStatusPhase getWorkerStatusPhase;

                    if (statusResponse.isSuccessful()) {
                        LOG.info("Status of worker '{}' at '{}' is '{}'", checkWorkersStatusWorker.getWorkerCurrentPreferences().getWorkerInternalId(), CommonDateTimeUtils.formatDateTimeToLog(checkWorkersStatusWorker.getGenerated()), statusResponse.getStatus().getText());

                        getWorkerStatusPhase = new GetWorkerStatusPhaseBuilder()
                                .result(WorkerStatusResult.OK)
                                .statusContext(statusResponse.toWorkerStatusContext())
                                .build();
                    } else {
                        if (statusResponse.getResult() == ComunytekStatusResult.MISSING_OR_INVALID_CREDENTIALS) {
                            getWorkerStatusPhase = new GetWorkerStatusPhaseBuilder()
                                    .result(WorkerStatusResult.MISSING_OR_INVALID_CREDENTIALS)
                                    .errorMessage(statusResponse.getErrorMessage())
                                    .build();
                        } else {
                            getWorkerStatusPhase = new GetWorkerStatusPhaseBuilder()
                                    .result(WorkerStatusResult.KO)
                                    .errorMessage(statusResponse.getErrorMessage())
                                    .build();
                        }

                        LOG.warn("Status of worker '{}' couldn't be retrieved. Reason: {}", checkWorkersStatusWorker.getWorkerCurrentPreferences().getWorkerInternalId(), getWorkerStatusPhase.getResult().name());
                    }

                    checkWorkersStatusWorker.setWorkerStatusPhase(getWorkerStatusPhase);

                    return Message.of(checkWorkersStatusWorker, message::ack);
                });
    }
}