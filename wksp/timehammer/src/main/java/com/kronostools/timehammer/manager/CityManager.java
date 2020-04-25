package com.kronostools.timehammer.manager;

import com.kronostools.timehammer.dao.CityDao;
import com.kronostools.timehammer.dto.CityDto;
import com.kronostools.timehammer.utils.Constants.Caches;
import io.quarkus.cache.CacheResult;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CityManager {
    private final CityDao cityDao;

    public CityManager(final CityDao cityDao) {
        this.cityDao = cityDao;
    }

    @CacheResult(cacheName = Caches.CITY_LIST)
    @Transactional
    public List<CityDto> getAllCities() {
        return cityDao.getAllCities();
    }
}
