package com.kronostools.timehammer.core.service;

import com.kronostools.timehammer.common.messages.constants.SimpleResult;
import com.kronostools.timehammer.common.messages.registration.SaveWorkerPhase;
import com.kronostools.timehammer.common.messages.registration.SaveWorkerPhaseBuilder;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessage;
import com.kronostools.timehammer.core.dao.WorkerCurrentPreferencesDao;
import com.kronostools.timehammer.core.dao.WorkerDao;
import com.kronostools.timehammer.core.model.InsertResult;
import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkerService {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerService.class);

    private final WorkerDao workerDao;
    private final WorkerCurrentPreferencesDao workerCurrentPreferencesDao;

    public WorkerService(final WorkerDao workerDao, final WorkerCurrentPreferencesDao workerCurrentPreferencesDao) {
        this.workerDao = workerDao;
        this.workerCurrentPreferencesDao = workerCurrentPreferencesDao;
    }

    public Uni<SaveWorkerPhase> saveWorker(final WorkerRegistrationRequestMessage registrationRequest) {
        // TODO: revisar errores si falla la búsqueda
        return workerCurrentPreferencesDao.findByExternalId(registrationRequest.getRegistrationRequestForm().getWorkerExternalId(), registrationRequest.getGenerated().toLocalDate())
                .onItem().produceUni(wcpsr -> {
                    final String registrationRequestId = wcpsr.isSuccessful() && wcpsr.hasResult() ? wcpsr.getResult().getWorkerInternalId() : registrationRequest.getRegistrationRequestId();

                    return workerDao.newWorker(registrationRequestId,
                            registrationRequest.getCheckWorkerCredentialsPhase().getFullname(),
                            registrationRequest.getCheckRegistrationRequestPhase().getChatId(),
                            registrationRequest.getRegistrationRequestForm())
                            .flatMap(this::getSaveWorkerPhaseFromInsertResult);
                });
    }

    private Uni<SaveWorkerPhase> getSaveWorkerPhaseFromInsertResult(final InsertResult ir) {
        final SaveWorkerPhase result;

        if (ir.isSuccessful()) {
            result = new SaveWorkerPhaseBuilder()
                    .result(SimpleResult.OK)
                    .build();
        } else {
            result = new SaveWorkerPhaseBuilder()
                    .result(SimpleResult.KO)
                    .errorMessage(ir.getErrorMessage())
                    .build();
        }

        return Uni.createFrom().item(result);
    }
}