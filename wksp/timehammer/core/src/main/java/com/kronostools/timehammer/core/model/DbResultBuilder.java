package com.kronostools.timehammer.core.model;

public interface DbResultBuilder<T> {
    T build();
    T buildUnsuccessful(final String errorMessage);
}
