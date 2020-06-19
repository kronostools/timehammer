package com.kronostools.timehammer.statemachine.processors;

import com.kronostools.timehammer.common.constants.CommonConstants.Channels;
import com.kronostools.timehammer.common.messages.constants.SimpleResult;
import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorker;
import com.kronostools.timehammer.common.messages.schedules.CheckWorkersStatusWorkerBuilder;
import com.kronostools.timehammer.common.messages.schedules.WorkerStatusActionPhaseBuilder;
import com.kronostools.timehammer.common.utils.CommonDateTimeUtils;
import com.kronostools.timehammer.statemachine.service.StateMachineService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkerStatusProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerStatusProcessor.class);

    private final StateMachineService stateMachineService;

    public WorkerStatusProcessor(final StateMachineService stateMachineService) {
        this.stateMachineService = stateMachineService;
    }

    @Incoming(Channels.STATUS_WORKER_PROCESS)
    @Outgoing(Channels.STATUS_WORKER_NOTIFY_OUT)
    public Uni<Message<CheckWorkersStatusWorker>> processWorkerStatus(final Message<CheckWorkersStatusWorker> message) {
        final CheckWorkersStatusWorker checkWorkersStatusWorker = CheckWorkersStatusWorkerBuilder.copy(message.getPayload()).build();

        if (checkWorkersStatusWorker.getWorkerStatusPhase().isSuccessful()) {
            LOG.info("Determining action corresponding to status '{}' at '{}' for worker '{}' ...",
                    checkWorkersStatusWorker.getWorkerStatusPhase().getStatusContext().name(),
                    CommonDateTimeUtils.formatDateTimeToLog(checkWorkersStatusWorker.getGenerated()),
                    checkWorkersStatusWorker.getWorkerCurrentPreferences().getWorkerInternalId());

            return stateMachineService.getAction(checkWorkersStatusWorker.getWorkerCurrentPreferences(), checkWorkersStatusWorker.getWorkerStatusPhase().getStatusContext())
                    .map(wsa -> {
                        LOG.info("Action corresponding to status '{}' at '{}' for worker '{}' is: {}",
                                checkWorkersStatusWorker.getWorkerStatusPhase().getStatusContext().name(),
                                CommonDateTimeUtils.formatDateTimeToLog(checkWorkersStatusWorker.getGenerated()),
                                checkWorkersStatusWorker.getWorkerCurrentPreferences().getWorkerInternalId(),
                                wsa.name());

                        checkWorkersStatusWorker.setWorkerStatusActionPhase(new WorkerStatusActionPhaseBuilder()
                                        .result(SimpleResult.OK)
                                        .workerStatusAction(wsa)
                                        .build());

                        return Message.of(checkWorkersStatusWorker, message::ack);
                    });
        } else {
            LOG.info("No action to determine because worker status could not be obtained. Reason: {}", checkWorkersStatusWorker.getWorkerStatusPhase().getResult().name());

            return Uni.createFrom().item(Message.of(checkWorkersStatusWorker, message::ack));
        }
    }
}