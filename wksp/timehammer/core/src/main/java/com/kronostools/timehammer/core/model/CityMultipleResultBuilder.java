package com.kronostools.timehammer.core.model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CityMultipleResultBuilder extends MultipleResultBuilder<CityMultipleResultBuilder, City> {

    public static CityMultipleResult buildFromResult(final List<City> cities) {
        return Optional.ofNullable(cities)
                .map(cl -> new CityMultipleResultBuilder()
                        .result(cl)
                        .build())
                .orElse(new CityMultipleResultBuilder()
                        .result(Collections.emptyList())
                        .build());
    }

    public static CityMultipleResult buildFromError(final String errorMessage) {
        return new CityMultipleResultBuilder()
                .errorMessage(errorMessage)
                .build();
    }

    public CityMultipleResult build() {
        return new CityMultipleResult(errorMessage, result);
    }
}
