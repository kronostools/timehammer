package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.registration.forms.RegistrationRequestForm;
import com.kronostools.timehammer.common.messages.schedules.ProcessableBatchScheduleMessageBuilder;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class WorkerRegistrationRequestMessageBuilder extends ProcessableBatchScheduleMessageBuilder<WorkerRegistrationRequestMessageBuilder> {
    private String registrationRequestId;
    private RegistrationRequestForm registrationRequestForm;
    private CheckRegistrationRequestPhase checkRegistrationRequestPhase;
    private CheckWorkerCredentialsPhase checkWorkerCredentialsPhase;
    private ValidateRegistrationRequestPhase validateRegistrationRequestPhase;
    private SaveWorkerPhase saveWorkerPhase;

    public static WorkerRegistrationRequestMessage copyAndBuild(final WorkerRegistrationRequestMessage registrationRequest) {
        return Optional.ofNullable(registrationRequest)
                .map(w -> WorkerRegistrationRequestMessageBuilder.copy(w).build())
                .orElse(null);
    }

    public static WorkerRegistrationRequestMessageBuilder copy(final WorkerRegistrationRequestMessage registrationRequest) {
        return Optional.ofNullable(registrationRequest)
                .map(w -> new WorkerRegistrationRequestMessageBuilder()
                    .generated(w.getGenerated())
                    .registrationRequestId(w.getRegistrationRequestId())
                    // TODO: deep copy
                    .registrationRequestForm(w.getRegistrationRequestForm())
                    .checkRegistrationRequestPhase(CheckRegistrationRequestPhaseBuilder.copyAndBuild(w.getCheckRegistrationRequestPhase()))
                    .checkWorkerCredentialsPhase(CheckWorkerCredentialsPhaseBuilder.copyAndBuild(w.getCheckWorkerCredentialsPhase()))
                    .validateRegistrationRequestPhase(ValidateRegistrationRequestPhaseBuilder.copyAndBuild(registrationRequest.getValidateRegistrationRequestPhase()))
                    .saveWorkerPhase(SaveWorkerPhaseBuilder.copyAndBuild(w.getSaveWorkerPhase())))
                .orElse(null);
    }

    public WorkerRegistrationRequestMessageBuilder registrationRequestId(final String registrationRequestId) {
        this.registrationRequestId = registrationRequestId;
        return this;
    }

    public WorkerRegistrationRequestMessageBuilder registrationRequestForm(final RegistrationRequestForm registrationRequestForm) {
        this.registrationRequestForm = registrationRequestForm;
        return this;
    }

    public WorkerRegistrationRequestMessageBuilder checkRegistrationRequestPhase(final CheckRegistrationRequestPhase checkRegistrationRequestPhase) {
        this.checkRegistrationRequestPhase = checkRegistrationRequestPhase;
        return this;
    }

    public WorkerRegistrationRequestMessageBuilder checkWorkerCredentialsPhase(final CheckWorkerCredentialsPhase checkWorkerCredentialsPhase) {
        this.checkWorkerCredentialsPhase = checkWorkerCredentialsPhase;
        return this;
    }

    public WorkerRegistrationRequestMessageBuilder validateRegistrationRequestPhase(final ValidateRegistrationRequestPhase validateRegistrationRequestPhase) {
        this.validateRegistrationRequestPhase = validateRegistrationRequestPhase;
        return this;
    }

    public WorkerRegistrationRequestMessageBuilder saveWorkerPhase(final SaveWorkerPhase saveWorkerPhase) {
        this.saveWorkerPhase = saveWorkerPhase;
        return this;
    }

    public WorkerRegistrationRequestMessage build() {
        final WorkerRegistrationRequestMessage result = new WorkerRegistrationRequestMessage(generated, registrationRequestId, registrationRequestForm);
        result.setCheckRegistrationRequestPhase(checkRegistrationRequestPhase);
        result.setCheckWorkerCredentialsPhase(checkWorkerCredentialsPhase);
        result.setValidateRegistrationRequestPhase(validateRegistrationRequestPhase);
        result.setSaveWorkerPhase(saveWorkerPhase);

        return result;
    }
}