package com.kronostools.timehammer.dto.form;

import com.kronostools.timehammer.dto.DemoTimestampForm;
import com.kronostools.timehammer.dto.form.validation.TimestampForm;

public class DemoTimestampFormValidationAdapter implements DemoTimestampFormValidation {
    private final DemoTimestampForm demoTimestampForm;

    public DemoTimestampFormValidationAdapter(final DemoTimestampForm demoTimestampForm) {
        this.demoTimestampForm = demoTimestampForm;
    }

    @Override
    public TimestampForm getTimestampForm() {
        TimestampForm result = new TimestampForm();
        result.setDay(demoTimestampForm.getDay());
        result.setMonth(demoTimestampForm.getMonth());
        result.setYear(demoTimestampForm.getYear());
        result.setHours(demoTimestampForm.getHours());
        result.setMinutes(demoTimestampForm.getMinutes());

        return result;
    }
}