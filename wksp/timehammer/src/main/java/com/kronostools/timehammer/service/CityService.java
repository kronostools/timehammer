package com.kronostools.timehammer.service;

import com.kronostools.timehammer.dto.CityDto;
import com.kronostools.timehammer.manager.CityManager;
import com.kronostools.timehammer.vo.CityVo;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CityService {
    private final CityManager cityManager;

    public CityService(final CityManager cityManager) {
        this.cityManager = cityManager;
    }

    public List<CityDto> getAllCities() {
        return cityManager.getAllCities();
    }

    public Optional<CityVo> findByCode(final String code) {
        return cityManager.findByCode(code);
    }
}