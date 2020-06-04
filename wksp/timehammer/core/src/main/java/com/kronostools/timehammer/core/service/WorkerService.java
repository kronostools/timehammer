package com.kronostools.timehammer.core.service;

import com.kronostools.timehammer.common.messages.constants.SaveWorkerResult;
import com.kronostools.timehammer.common.messages.registration.SaveWorkerPhase;
import com.kronostools.timehammer.common.messages.registration.SaveWorkerPhaseBuilder;
import com.kronostools.timehammer.common.messages.registration.WorkerRegistrationRequestMessage;
import com.kronostools.timehammer.core.dao.WorkerChatDao;
import com.kronostools.timehammer.core.dao.WorkerDao;
import com.kronostools.timehammer.core.dao.WorkerPreferencesDao;
import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

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

    @Transactional
    public Uni<SaveWorkerPhase> saveWorker(final WorkerRegistrationRequestMessage registrationRequest) {
        return Uni.createFrom().item(registrationRequest)
                .onItem()
                    .invoke(wrr -> workerDao.insertWorker(wrr.getRegistrationRequestForm().getWorkerInternalId(), wrr.getCheckWorkerCredentialsPhase().getFullname()))
                .onItem()
                    .invoke(wrr -> workerChatDao.insertWorkerChat(wrr.getRegistrationRequestForm().getWorkerInternalId(), wrr.getCheckRegistrationRequestPhase().getChatId()))
                .onItem()
                    .invoke(wrr -> workerPreferencesDao.insertWorkerPreferences(wrr.getRegistrationRequestForm().getWorkerInternalId(),
                                                                                wrr.getRegistrationRequestForm().getWorkerExternalId(),
                                                                                wrr.getRegistrationRequestForm().getDefaultTimetable(),
                                                                                wrr.getRegistrationRequestForm().getCompany(),
                                                                                wrr.getRegistrationRequestForm().getWorkSsid(),
                                                                                wrr.getRegistrationRequestForm().getWorkCity()))
                .map(ir -> new SaveWorkerPhaseBuilder()
                                .result(SaveWorkerResult.OK)
                                .build());
    }
}