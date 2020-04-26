package com.kronostools.timehammer.service;

import com.kronostools.timehammer.dto.CityDto;
import com.kronostools.timehammer.manager.CityManager;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CityService {
    private final CityManager cityManager;

    public CityService(final CityManager cityManager) {
        this.cityManager = cityManager;
    }

    public List<CityDto> getAllCities() {
        return cityManager.getAllCities();
    }

    public boolean cityByCodeExists(final String code) {
        return cityManager.cityByCodeExists(code);
    }
}