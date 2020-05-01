package com.kronostools.timehammer.dto.form;

import com.kronostools.timehammer.dto.UpdatePasswordForm;

public class UpdatePasswordFormValidationAdapter implements UpdatePasswordFormValidation {
    private final UpdatePasswordForm updatePasswordForm;

    public UpdatePasswordFormValidationAdapter(final UpdatePasswordForm updatePasswordForm) {
        this.updatePasswordForm = updatePasswordForm;
    }

    @Override
    public String getInternalId() {
        return updatePasswordForm.getInternalId();
    }

    @Override
    public String getExternalPassword() {
        return updatePasswordForm.getExternalPassword();
    }
}