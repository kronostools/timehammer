package com.kronostools.timehammer.core.model;

import java.util.List;

public class CityMultipleResult extends MultipleResult<City> {
    public CityMultipleResult(final String errorMessage, final List<City> cities) {
        super(errorMessage);
        this.result = cities;
    }
}