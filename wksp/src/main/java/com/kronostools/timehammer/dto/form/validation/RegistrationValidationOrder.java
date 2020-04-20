package com.kronostools.timehammer.dto.form.validation;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, RegistrationValidationGroup2.class})
public interface RegistrationValidationOrder {
}
