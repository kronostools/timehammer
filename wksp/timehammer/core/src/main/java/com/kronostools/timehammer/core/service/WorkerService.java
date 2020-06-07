package com.kronostools.timehammer.core.service;

import com.kronostools.timehammer.common.messages.constants.SimpleResult;
import com.kronostools.timehammer.common.messages.registration.SaveWorkerPhase;
import com.kronostools.timehammer.common.messages.registration.SaveWorkerPhaseBuilder;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessage;
import com.kronostools.timehammer.core.dao.WorkerChatDao;
import com.kronostools.timehammer.core.dao.WorkerDao;
import com.kronostools.timehammer.core.dao.WorkerPreferencesDao;
import com.kronostools.timehammer.core.model.InsertResult;
import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

@ApplicationScoped
public class WorkerService {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerService.class);

    private final WorkerDao workerDao;
    private final WorkerChatDao workerChatDao;
    private final WorkerPreferencesDao workerPreferencesDao;

    public WorkerService(final WorkerDao workerDao,
                         final WorkerChatDao workerChatDao,
                         final WorkerPreferencesDao workerPreferencesDao) {
        this.workerDao = workerDao;
        this.workerChatDao = workerChatDao;
        this.workerPreferencesDao = workerPreferencesDao;
    }

    @Transactional(TxType.REQUIRED)
    public Uni<SaveWorkerPhase> saveWorker(final WorkerRegistrationRequestMessage registrationRequest) {
        final SaveWorkerPhase initialSaveWorkerPhase = new SaveWorkerPhaseBuilder().result(SimpleResult.OK).build();

        return Uni.createFrom().item(initialSaveWorkerPhase)
                // Save worker
                .flatMap(swp -> {
                    LOG.debug("(1) Saving worker '{}' ...", registrationRequest.getRegistrationRequestId());

                    return workerDao.insertWorker(registrationRequest.getRegistrationRequestId(), registrationRequest.getCheckWorkerCredentialsPhase().getFullname());
                })
                // Save worker chat
                .flatMap(ir -> {
                    LOG.debug("(2) Saving chat '{}' of worker '{}' ...", registrationRequest.getCheckRegistrationRequestPhase().getChatId(), registrationRequest.getRegistrationRequestId());

                    return workerChatDao.insertWorkerChat(registrationRequest.getRegistrationRequestId(), registrationRequest.getCheckRegistrationRequestPhase().getChatId());
                })
                // Save worker preferences
                .flatMap(ir -> {
                    LOG.debug("(3) Saving preferences of worker '{}' ...", registrationRequest.getRegistrationRequestId());

                    return workerPreferencesDao.insertWorkerPreferences(registrationRequest.getRegistrationRequestId(),
                            registrationRequest.getRegistrationRequestForm().getWorkerExternalId(),
                            registrationRequest.getRegistrationRequestForm().getDefaultTimetable(),
                            registrationRequest.getRegistrationRequestForm().getCompanyCode(),
                            registrationRequest.getRegistrationRequestForm().getWorkSsid(),
                            registrationRequest.getRegistrationRequestForm().getWorkCity());
                })
                // Process save worker preferences result
                .flatMap(ir -> Uni.createFrom().item(new SaveWorkerPhaseBuilder().result(SimpleResult.OK).build()));

        /*
        return Uni.createFrom().item(initialSaveWorkerPhase)
                // Save worker
                .flatMap(swp -> {
                    LOG.debug("(1) Saving worker '{}' ...", registrationRequest.getRegistrationRequestId());

                    return workerDao.insertWorker(registrationRequest.getRegistrationRequestId(), registrationRequest.getCheckWorkerCredentialsPhase().getFullname());
                })
                // Process save worker result
                .flatMap(this::getSaveWorkerPhaseFromInsertResult)
                // Save worker chat
                .flatMap(swp -> {
                    if (swp.isSuccessful()) {
                        LOG.debug("(2) Saving chat '{}' of worker '{}' ...", registrationRequest.getCheckRegistrationRequestPhase().getChatId(), registrationRequest.getRegistrationRequestId());

                        return workerChatDao.insertWorkerChat(registrationRequest.getRegistrationRequestId(), registrationRequest.getCheckRegistrationRequestPhase().getChatId());
                    } else {
                        return Uni.createFrom().item(new InsertResultBuilder()
                                .errorMessage(swp.getErrorMessage())
                                .build());
                    }
                })
                // Process save worker chat result
                .flatMap(this::getSaveWorkerPhaseFromInsertResult)
                // Save worker preferences
                .flatMap(swp -> {
                    if (swp.isSuccessful()) {
                        LOG.debug("(3) Saving preferences of worker '{}' ...", registrationRequest.getRegistrationRequestId());

                        return workerPreferencesDao.insertWorkerPreferences(registrationRequest.getRegistrationRequestId(),
                                registrationRequest.getRegistrationRequestForm().getWorkerExternalId(),
                                registrationRequest.getRegistrationRequestForm().getDefaultTimetable(),
                                registrationRequest.getRegistrationRequestForm().getCompanyCode(),
                                registrationRequest.getRegistrationRequestForm().getWorkSsid(),
                                registrationRequest.getRegistrationRequestForm().getWorkCity());
                    } else {
                        return Uni.createFrom().item(new InsertResultBuilder()
                                .errorMessage(swp.getErrorMessage())
                                .build());
                    }
                })
                // Process save worker preferences result
                .flatMap(this::getSaveWorkerPhaseFromInsertResult);
        */

        /*
        return Uni.createFrom().item(registrationRequest)
                .then(self -> self
                        .onItem().apply(wrr -> {
                            LOG.debug("(1) Saving worker '{}' ...", wrr.getRegistrationRequestId());

                            return workerDao.insertWorker(wrr.getRegistrationRequestId(), wrr.getCheckWorkerCredentialsPhase().getFullname());
                        })
                        .onItem().apply(ir -> registrationRequest))
                .then(self -> self
                        .onItem().apply(wrr -> {
                            LOG.debug("(2) Saving chat '{}' of worker '{}' ...", wrr.getCheckRegistrationRequestPhase().getChatId(), wrr.getRegistrationRequestId());

                            return workerChatDao.insertWorkerChat(wrr.getRegistrationRequestId(), wrr.getCheckRegistrationRequestPhase().getChatId());
                        })
                        .onItem().apply(ir -> registrationRequest))
                .then(self -> self
                        .onItem().apply(wrr -> {
                            LOG.debug("(3) Saving preferences of worker '{}' ...", wrr.getRegistrationRequestId());

                            return workerPreferencesDao.insertWorkerPreferences(wrr.getRegistrationRequestId(),
                                    wrr.getRegistrationRequestForm().getWorkerExternalId(),
                                    wrr.getRegistrationRequestForm().getDefaultTimetable(),
                                    wrr.getRegistrationRequestForm().getCompanyCode(),
                                    wrr.getRegistrationRequestForm().getWorkSsid(),
                                    wrr.getRegistrationRequestForm().getWorkCity());
                        })
                        .onItem().apply(ir -> registrationRequest))
                .then(self -> self.onItem().apply(wrr -> new SaveWorkerPhaseBuilder()
                        .result(SimpleResult.OK)
                        .build()));
        */

        /*
        return Uni.createFrom().item(registrationRequest)
                .onItem()
                .invoke(wrr -> {
                    LOG.debug("(1) Saving worker '{}' ...", wrr.getRegistrationRequestId());

                    workerDao.insertWorker(wrr.getRegistrationRequestId(), wrr.getCheckWorkerCredentialsPhase().getFullname());
                })
                .onItem()
                .invoke(wrr -> {
                    LOG.debug("(2) Saving chat '{}' of worker '{}' ...", wrr.getCheckRegistrationRequestPhase().getChatId(), wrr.getRegistrationRequestId());

                    workerChatDao.insertWorkerChat(wrr.getRegistrationRequestId(), wrr.getCheckRegistrationRequestPhase().getChatId());
                })
                .onItem()
                .invoke(wrr -> {
                    LOG.debug("(3) Saving preferences of worker '{}' ...", wrr.getRegistrationRequestId());

                    workerPreferencesDao.insertWorkerPreferences(wrr.getRegistrationRequestId(),
                            wrr.getRegistrationRequestForm().getWorkerExternalId(),
                            wrr.getRegistrationRequestForm().getDefaultTimetable(),
                            wrr.getRegistrationRequestForm().getCompanyCode(),
                            wrr.getRegistrationRequestForm().getWorkSsid(),
                            wrr.getRegistrationRequestForm().getWorkCity());
                })
                .map(ir -> new SaveWorkerPhaseBuilder()
                        .result(SimpleResult.OK)
                        .build());
        */
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