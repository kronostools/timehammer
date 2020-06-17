package com.kronostools.timehammer.comunytek.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.constants.WorkerStatusContext;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;
import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorker;
import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorkerBuilder;
import com.kronostools.timehammer.common.messages.schedules.GetWorkerStatusPhase;
import com.kronostools.timehammer.common.messages.schedules.GetWorkerStatusPhaseBuilder;
import com.kronostools.timehammer.comunytek.client.ComunytekClient;
import com.kronostools.timehammer.comunytek.constants.ComunytekSimpleResult;
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
    @Outgoing(Channels.STATUS_WORKER_UPDATE)
    public Uni<Message<CheckWorkersStatusWorker>> retrieveHolidays(final Message<CheckWorkersStatusWorker> message) {
        final CheckWorkersStatusWorker checkWorkersStatusWorker = CheckWorkersStatusWorkerBuilder.copy(message.getPayload()).build();

        return comunytekClient
                .getStatus(checkWorkersStatusWorker.getWorkerExternalId(), checkWorkersStatusWorker.getGenerated())
                .onFailure(Exception.class)
                    .recoverWithItem((e) -> new ComunytekStatusResponseBuilder()
                            .result(ComunytekSimpleResult.KO)
                            .errorMessage(e.getMessage())
                            .build())
                .map(statusResponse -> {
                    final GetWorkerStatusPhase getWorkerStatusPhase;

                    if (statusResponse.isSuccessful()) {
                        WorkerStatusContext statusContext = null;

                        switch (statusResponse.getStatus()) {
                            case INITIAL:
                                statusContext = WorkerStatusContext.BEFORE_WORK;
                                break;
                            case STARTED:
                            case RESUMED:
                                statusContext = WorkerStatusContext.WORK;
                                break;
                            case PAUSED:
                                if (statusResponse.getComment() != null && statusResponse.getComment().toLowerCase().contains("comida")) {
                                    statusContext = WorkerStatusContext.LUNCH;
                                }
                                break;
                            case ENDED:
                                statusContext = WorkerStatusContext.AFTER_WORK;
                                break;
                        }

                        getWorkerStatusPhase = new GetWorkerStatusPhaseBuilder()
                                .result(SimpleResult.OK)
                                .statusContext(statusContext)
                                .build();
                    } else {
                        LOG.warn("Status of worker '{}' couldn't be retrieved", checkWorkersStatusWorker.getWorkerInternalId());

                        // TODO: differentiate betweeen unexpected error and missing/invalid credentials
                        getWorkerStatusPhase = new GetWorkerStatusPhaseBuilder()
                                .result(SimpleResult.KO)
                                .errorMessage(statusResponse.getErrorMessage())
                                .build();
                    }

                    checkWorkersStatusWorker.setWorkerStatusPhase(getWorkerStatusPhase);

                    return Message.of(checkWorkersStatusWorker, message::ack);
                });
    }
}