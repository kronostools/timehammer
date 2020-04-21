package com.kronostools.timehammer.manager;

import com.kronostools.timehammer.dao.CityDao;
import com.kronostools.timehammer.dto.CityDto;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CityManager {
    private final CityDao cityDao;

    public CityManager(final CityDao cityDao) {
        this.cityDao = cityDao;
    }

    // TODO: add cache
    public List<CityDto> getAllCitiesCached() {
        return cityDao.getAllCities();
    }
}
