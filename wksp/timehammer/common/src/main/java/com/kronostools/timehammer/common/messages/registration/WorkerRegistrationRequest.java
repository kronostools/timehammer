package com.kronostools.timehammer.common.messages.registration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kronostools.timehammer.common.messages.PlatformMessage;
import com.kronostools.timehammer.common.messages.registration.forms.RegistrationRequestForm;

import java.time.LocalDateTime;

@JsonDeserialize(builder = WorkerRegistrationRequestBuilder.class)
public class WorkerRegistrationRequest extends PlatformMessage {
    private RegistrationRequestForm registrationRequestForm;
    private CheckRegistrationRequestPhase checkRegistrationRequestPhase;
    private CheckWorkerCredentialsPhase checkWorkerCredentialsPhase;
    private ValidateRegistrationRequestPhase validateRegistrationRequestPhase;
    private SaveWorkerPhase saveWorkerPhase;

    WorkerRegistrationRequest(final LocalDateTime generated, final RegistrationRequestForm registrationRequestForm) {
        super(generated);
        this.registrationRequestForm = registrationRequestForm;
    }

    // TODO: revisar si hace falta este método
    // TODO: evaluar si crear otra clase que extienda de PlatformMessage y obligue a tener este método
    /*
    public boolean processedSuccessfully() {
        return Optional.ofNullable(saveHolidayPhase)
                .map(SaveHolidayPhase::isSuccessful)
                .orElse(false);
    }
    */

    public RegistrationRequestForm getRegistrationRequestForm() {
        return registrationRequestForm;
    }

    public void setRegistrationRequestForm(RegistrationRequestForm registrationRequestForm) {
        this.registrationRequestForm = registrationRequestForm;
    }

    public CheckRegistrationRequestPhase getCheckRegistrationRequestPhase() {
        return checkRegistrationRequestPhase;
    }

    public void setCheckRegistrationRequestPhase(CheckRegistrationRequestPhase checkRegistrationRequestPhase) {
        this.checkRegistrationRequestPhase = checkRegistrationRequestPhase;
    }

    public CheckWorkerCredentialsPhase getCheckWorkerCredentialsPhase() {
        return checkWorkerCredentialsPhase;
    }

    public void setCheckWorkerCredentialsPhase(CheckWorkerCredentialsPhase checkWorkerCredentialsPhase) {
        this.checkWorkerCredentialsPhase = checkWorkerCredentialsPhase;
    }

    public ValidateRegistrationRequestPhase getValidateRegistrationRequestPhase() {
        return validateRegistrationRequestPhase;
    }

    public void setValidateRegistrationRequestPhase(ValidateRegistrationRequestPhase validateRegistrationRequestPhase) {
        this.validateRegistrationRequestPhase = validateRegistrationRequestPhase;
    }

    public SaveWorkerPhase getSaveWorkerPhase() {
        return saveWorkerPhase;
    }

    public void setSaveWorkerPhase(SaveWorkerPhase saveWorkerPhase) {
        this.saveWorkerPhase = saveWorkerPhase;
    }
}