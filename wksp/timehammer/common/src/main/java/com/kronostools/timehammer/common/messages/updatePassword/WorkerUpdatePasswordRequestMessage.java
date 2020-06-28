package com.kronostools.timehammer.common.messages.updatePassword;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.PlatformMessage;
import com.kronostools.timehammer.common.messages.updatePassword.forms.UpdatePasswordRequestForm;

import java.time.LocalDateTime;

@JsonDeserialize(builder = WorkerUpdatePasswordRequestMessageBuilder.class)
public class WorkerUpdatePasswordRequestMessage extends PlatformMessage {
    private final String requestId;
    private UpdatePasswordRequestForm updatePasswordRequestForm;
    private CheckUpdatePasswordRequestPhase checkUpdatePasswordRequestPhase;
    private ValidateUpdatePasswordRequestPhase validateUpdatePasswordRequestPhase;
    private CheckUpdatePasswordCredentialsPhase checkUpdatePasswordCredentialsPhase;

    WorkerUpdatePasswordRequestMessage(final LocalDateTime generated, final String requestId, final UpdatePasswordRequestForm updatePasswordRequestForm) {
        super(generated);
        this.requestId = requestId;
        this.updatePasswordRequestForm = updatePasswordRequestForm;
    }

    public String getRequestId() {
        return requestId;
    }

    public UpdatePasswordRequestForm getUpdatePasswordRequestForm() {
        return updatePasswordRequestForm;
    }

    public void setUpdatePasswordRequestForm(UpdatePasswordRequestForm updatePasswordRequestForm) {
        this.updatePasswordRequestForm = updatePasswordRequestForm;
    }

    public CheckUpdatePasswordRequestPhase getCheckUpdatePasswordRequestPhase() {
        return checkUpdatePasswordRequestPhase;
    }

    public void setCheckUpdatePasswordRequestPhase(CheckUpdatePasswordRequestPhase checkUpdatePasswordRequestPhase) {
        this.checkUpdatePasswordRequestPhase = checkUpdatePasswordRequestPhase;
    }

    public ValidateUpdatePasswordRequestPhase getValidateUpdatePasswordRequestPhase() {
        return validateUpdatePasswordRequestPhase;
    }

    public void setValidateUpdatePasswordRequestPhase(ValidateUpdatePasswordRequestPhase validateUpdatePasswordRequestPhase) {
        this.validateUpdatePasswordRequestPhase = validateUpdatePasswordRequestPhase;
    }

    public CheckUpdatePasswordCredentialsPhase getCheckUpdatePasswordCredentialsPhase() {
        return checkUpdatePasswordCredentialsPhase;
    }

    public void setCheckUpdatePasswordCredentialsPhase(CheckUpdatePasswordCredentialsPhase checkUpdatePasswordCredentialsPhase) {
        this.checkUpdatePasswordCredentialsPhase = checkUpdatePasswordCredentialsPhase;
    }
}