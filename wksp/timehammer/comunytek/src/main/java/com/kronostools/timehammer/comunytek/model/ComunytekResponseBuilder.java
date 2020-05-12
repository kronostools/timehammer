package com.kronostools.timehammer.comunytek.model;

public interface ComunytekResponseBuilder<T> {
    T build();
    T buildUnsuccessful(final String errorMessage);
}
