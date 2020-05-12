package com.kronostools.timehammer.common.messages.schedules;

public interface ResultBuilder<T> {
    T build();
    T buildUnsuccessful(final String errorMessage);
}
