package com.kronostools.timehammer.dto.form;

import javax.validation.constraints.NotBlank;

public interface UpdatePasswordFormValidation {
    @NotBlank
    String getInternalId();

    @NotBlank
    String getExternalPassword();
}