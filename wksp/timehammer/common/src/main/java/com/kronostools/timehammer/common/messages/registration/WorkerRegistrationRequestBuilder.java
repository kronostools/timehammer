package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.registration.forms.RegistrationRequestForm;
import com.kronostools.timehammer.common.messages.schedules.ProcessableBatchScheduleMessageBuilder;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class WorkerRegistrationRequestBuilder extends ProcessableBatchScheduleMessageBuilder<WorkerRegistrationRequestBuilder> {
    private RegistrationRequestForm registrationRequestForm;
    private CheckRegistrationRequestPhase checkRegistrationRequestPhase;
    private CheckWorkerCredentialsPhase checkWorkerCredentialsPhase;
    private ValidateRegistrationRequestPhase validateRegistrationRequestPhase;
    private SaveWorkerPhase saveWorkerPhase;

    public static WorkerRegistrationRequest copyAndBuild(final WorkerRegistrationRequest registrationRequest) {
        return Optional.ofNullable(registrationRequest)
                .map(w -> WorkerRegistrationRequestBuilder.copy(w).build())
                .orElse(null);
    }

    public static WorkerRegistrationRequestBuilder copy(final WorkerRegistrationRequest registrationRequest) {
        return Optional.ofNullable(registrationRequest)
                .map(w -> new WorkerRegistrationRequestBuilder()
                    .generated(w.getGenerated())
                    // TODO: deep copy
                    .registrationRequestForm(w.getRegistrationRequestForm())
                    .checkRegistrationRequestPhase(CheckRegistrationRequestPhaseBuilder.copyAndBuild(w.getCheckRegistrationRequestPhase()))
                    .checkWorkerCredentialsPhase(CheckWorkerCredentialsPhaseBuilder.copyAndBuild(w.getCheckWorkerCredentialsPhase()))
                    .validateRegistrationRequestPhase(ValidateRegistrationRequestPhaseBuilder.copyAndBuild(registrationRequest.getValidateRegistrationRequestPhase()))
                    .saveWorkerPhase(SaveWorkerPhaseBuilder.copyAndBuild(w.getSaveWorkerPhase())))
                .orElse(null);
    }

    public WorkerRegistrationRequestBuilder registrationRequestForm(final RegistrationRequestForm registrationRequestForm) {
        this.registrationRequestForm = registrationRequestForm;
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

    public WorkerRegistrationRequestBuilder saveWorkerPhase(final SaveWorkerPhase saveWorkerPhase) {
        this.saveWorkerPhase = saveWorkerPhase;
        return this;
    }

    public WorkerRegistrationRequest build() {
        final WorkerRegistrationRequest result = new WorkerRegistrationRequest(generated, registrationRequestForm);
        result.setCheckRegistrationRequestPhase(checkRegistrationRequestPhase);
        result.setCheckWorkerCredentialsPhase(checkWorkerCredentialsPhase);
        result.setValidateRegistrationRequestPhase(validateRegistrationRequestPhase);
        result.setSaveWorkerPhase(saveWorkerPhase);

        return result;
    }
}