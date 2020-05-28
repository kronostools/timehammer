package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.constants.Company;
import com.kronostools.timehammer.common.messages.schedules.ProcessableBatchScheduleMessageBuilder;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class WorkerRegistrationRequestBuilder extends ProcessableBatchScheduleMessageBuilder<WorkerRegistrationRequestBuilder> {
    private String workerInternalId;
    private Company company;
    private String workerExternalId;
    private String workerExternalPassword;
    private String workCity;
    private String workSsid;
    // TODO: add timetable
    private CheckRegistrationRequestPhase checkRegistrationRequestPhase;
    private CheckWorkerCredentialsPhase checkWorkerCredentialsPhase;
    private ValidateRegistrationRequestPhase validateRegistrationRequestPhase;

    public static WorkerRegistrationRequest copyAndBuild(final WorkerRegistrationRequest worker) {
        return Optional.ofNullable(worker)
                .map(w -> WorkerRegistrationRequestBuilder.copy(w).build())
                .orElse(null);
    }

    public static WorkerRegistrationRequestBuilder copy(final WorkerRegistrationRequest worker) {
        return Optional.ofNullable(worker)
                .map(w -> new WorkerRegistrationRequestBuilder()
                    .generated(w.getGenerated())
                    .workerInternalId(w.getWorkerInternalId())
                    .company(w.getCompany())
                    .workerExternalId(w.getWorkerExternalId())
                    .workerExternalPassword(w.getWorkerExternalPassword())
                    .workCity(w.getWorkCity())
                    .workSsid(w.getWorkSsid())
                    .checkRegistrationRequestPhase(CheckRegistrationRequestPhaseBuilder.copyAndBuild(w.getCheckRegistrationRequestPhase()))
                    .checkWorkerCredentialsPhase(CheckWorkerCredentialsPhaseBuilder.copyAndBuild(w.getCheckWorkerCredentialsPhase()))
                    .validateRegistrationRequestPhase(ValidateRegistrationRequestPhaseBuilder.copyAndBuild(worker.getValidateRegistrationRequestPhase())))
                .orElse(null);
    }

    public WorkerRegistrationRequestBuilder workerInternalId(final String workerInternalId) {
        this.workerInternalId = workerInternalId;
        return this;
    }

    public WorkerRegistrationRequestBuilder company(final Company company) {
        this.company = company;
        return this;
    }

    public WorkerRegistrationRequestBuilder workerExternalId(final String workerExternalId) {
        this.workerExternalId = workerExternalId;
        return this;
    }

    public WorkerRegistrationRequestBuilder workerExternalPassword(final String workerExternalPassword) {
        this.workerExternalPassword = workerExternalPassword;
        return this;
    }

    public WorkerRegistrationRequestBuilder workCity(final String workCity) {
        this.workCity = workCity;
        return this;
    }

    public WorkerRegistrationRequestBuilder workSsid(final String workSsid) {
        this.workSsid = workSsid;
        return this;
    }

    public WorkerRegistrationRequestBuilder checkRegistrationRequestPhase(final CheckRegistrationRequestPhase checkRegistrationRequestPhase) {
        this.checkRegistrationRequestPhase = checkRegistrationRequestPhase;
        return this;
    }

    public WorkerRegistrationRequestBuilder checkWorkerCredentialsPhase(final CheckWorkerCredentialsPhase checkWorkerCredentialsPhase) {
        this.checkWorkerCredentialsPhase = checkWorkerCredentialsPhase;
        return this;
    }

    public WorkerRegistrationRequestBuilder validateRegistrationRequestPhase(final ValidateRegistrationRequestPhase validateRegistrationRequestPhase) {
        this.validateRegistrationRequestPhase = validateRegistrationRequestPhase;
        return this;
    }

    public WorkerRegistrationRequest build() {
        final WorkerRegistrationRequest result = new WorkerRegistrationRequest(generated, workerInternalId);
        result.setCompany(company);
        result.setWorkerExternalId(workerExternalId);
        result.setWorkerExternalPassword(workerExternalPassword);
        result.setWorkCity(workCity);
        result.setWorkSsid(workSsid);
        result.setCheckRegistrationRequestPhase(checkRegistrationRequestPhase);
        result.setCheckWorkerCredentialsPhase(checkWorkerCredentialsPhase);
        result.setValidateRegistrationRequestPhase(validateRegistrationRequestPhase);

        return result;
    }
}