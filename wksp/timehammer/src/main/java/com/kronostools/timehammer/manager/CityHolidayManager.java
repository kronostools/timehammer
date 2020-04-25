package com.kronostools.timehammer.manager;

import com.kronostools.timehammer.dao.CityHolidayDao;
import com.kronostools.timehammer.utils.Constants.Caches;
import com.kronostools.timehammer.vo.HolidayVo;
import io.quarkus.cache.CacheResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class CityHolidayManager {
    private static final Logger LOG = LoggerFactory.getLogger(CityHolidayManager.class);

    private final CityHolidayDao cityHolidayDao;

    public CityHolidayManager(final CityHolidayDao cityHolidayDao) {
        this.cityHolidayDao = cityHolidayDao;
    }

    public Set<LocalDate> getAllCityHolidays(final String cityCode) {
        LOG.debug("BEGIN getCityHolidays: [{}]", cityCode);

        List<HolidayVo> holidayVoList = cityHolidayDao.fetchAllCityHolidayAsHolidayVoByCityCode(cityCode);

        Set<LocalDate> holidays = holidayVoList.stream().map(HolidayVo::getDay).collect(Collectors.toCollection(LinkedHashSet::new));

        LOG.debug("END getAllCityHolidays");

        return holidays;
    }

    @CacheResult(cacheName = Caches.CITY_HOLIDAYS)
    @Transactional
    public Set<LocalDate> getPendingCityHolidays(final String cityCode) {
        LOG.debug("BEGIN getPendingCityHolidays: [{}]", cityCode);

        List<HolidayVo> holidayVoList = cityHolidayDao.fetchPendingCityHolidayAsHolidayVoByCityCode(cityCode);

        Set<LocalDate> holidays = holidayVoList.stream().map(HolidayVo::getDay).collect(Collectors.toCollection(LinkedHashSet::new));

        LOG.debug("END getPendingCityHolidays");

        return holidays;
    }
}