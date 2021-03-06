package com.kronostools.timehammer.common.messages.updatePassword;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.kronostools.timehammer.common.messages.schedules.ProcessableBatchScheduleMessageBuilder;
import com.kronostools.timehammer.common.messages.updatePassword.forms.UpdatePasswordRequestForm;

import java.util.Optional;

@JsonPOJOBuilder(withPrefix = "")
public class WorkerUpdatePasswordRequestMessageBuilder extends ProcessableBatchScheduleMessageBuilder<WorkerUpdatePasswordRequestMessageBuilder> {
    private String requestId;
    private UpdatePasswordRequestForm updatePasswordRequestForm;
    private CheckUpdatePasswordRequestPhase checkUpdatePasswordRequestPhase;
    private ValidateUpdatePasswordRequestPhase validateUpdatePasswordRequestPhase;
    private CheckUpdatePasswordCredentialsPhase checkUpdatePasswordCredentialsPhase;

    public static WorkerUpdatePasswordRequestMessage copyAndBuild(final WorkerUpdatePasswordRequestMessage updatePasswordRequest) {
        return Optional.ofNullable(updatePasswordRequest)
                .map(w -> WorkerUpdatePasswordRequestMessageBuilder.copy(w).build())
                .orElse(null);
    }

    public static WorkerUpdatePasswordRequestMessageBuilder copy(final WorkerUpdatePasswordRequestMessage updatePasswordRequest) {
        return Optional.ofNullable(updatePasswordRequest)
                .map(w -> new WorkerUpdatePasswordRequestMessageBuilder()
                    .generated(w.getGenerated())
                    .requestId(w.getRequestId())
                    // TODO: deep copy
                    .updatePasswordRequestForm(w.getUpdatePasswordRequestForm())
                    .checkUpdatePasswordRequestPhase(CheckUpdatePasswordRequestPhaseBuilder.copyAndBuild(w.getCheckUpdatePasswordRequestPhase()))
                    .validateUpdatePasswordRequestPhase(ValidateUpdatePasswordRequestPhaseBuilder.copyAndBuild(updatePasswordRequest.getValidateUpdatePasswordRequestPhase()))
                    .checkUpdatePasswordCredentialsPhase(CheckUpdatePasswordCredentialsPhaseBuilder.copyAndBuild(w.getCheckUpdatePasswordCredentialsPhase())))
                .orElse(null);
    }

    public WorkerUpdatePasswordRequestMessageBuilder requestId(final String requestId) {
        this.requestId = requestId;
        return this;
    }

    public WorkerUpdatePasswordRequestMessageBuilder updatePasswordRequestForm(final UpdatePasswordRequestForm updatePasswordRequestForm) {
        this.updatePasswordRequestForm = updatePasswordRequestForm;
        return this;
    }

    public WorkerUpdatePasswordRequestMessageBuilder checkUpdatePasswordRequestPhase(final CheckUpdatePasswordRequestPhase checkUpdatePasswordRequestPhase) {
        this.checkUpdatePasswordRequestPhase = checkUpdatePasswordRequestPhase;
        return this;
    }

    public WorkerUpdatePasswordRequestMessageBuilder validateUpdatePasswordRequestPhase(final ValidateUpdatePasswordRequestPhase validateUpdatePasswordRequestPhase) {
        this.validateUpdatePasswordRequestPhase = validateUpdatePasswordRequestPhase;
        return this;
    }

    public WorkerUpdatePasswordRequestMessageBuilder checkUpdatePasswordCredentialsPhase(final CheckUpdatePasswordCredentialsPhase checkUpdatePasswordCredentialsPhase) {
        this.checkUpdatePasswordCredentialsPhase = checkUpdatePasswordCredentialsPhase;
        return this;
    }

    public WorkerUpdatePasswordRequestMessage build() {
        final WorkerUpdatePasswordRequestMessage result = new WorkerUpdatePasswordRequestMessage(generated, requestId, updatePasswordRequestForm);
        result.setCheckUpdatePasswordRequestPhase(checkUpdatePasswordRequestPhase);
        result.setValidateUpdatePasswordRequestPhase(validateUpdatePasswordRequestPhase);
        result.setCheckUpdatePasswordCredentialsPhase(checkUpdatePasswordCredentialsPhase);

        return result;
    }
}