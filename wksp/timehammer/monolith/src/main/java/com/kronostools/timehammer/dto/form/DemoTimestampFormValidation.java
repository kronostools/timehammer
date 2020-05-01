package com.kronostools.timehammer.dto.form;

import com.kronostools.timehammer.dto.form.validation.TimestampForm;
import com.kronostools.timehammer.dto.form.validation.TimestampFormValid;

public interface DemoTimestampFormValidation {
    @TimestampFormValid
    TimestampForm getTimestampForm();
}