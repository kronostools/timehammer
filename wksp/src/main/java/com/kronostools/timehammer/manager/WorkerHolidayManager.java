package com.kronostools.timehammer.manager;

import com.kronostools.timehammer.comunytek.dto.ComunytekHolidaysDto;
import com.kronostools.timehammer.dao.WorkerHolidayDao;
import com.kronostools.timehammer.model.WorkerHoliday;
import com.kronostools.timehammer.model.WorkerHolidayId;
import com.kronostools.timehammer.utils.Constants.Caches;
import com.kronostools.timehammer.vo.HolidayVo;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class WorkerHolidayManager {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerHolidayManager.class);

    private final WorkerHolidayDao workerHolidayDao;

    public WorkerHolidayManager(final WorkerHolidayDao workerHolidayDao) {
        this.workerHolidayDao = workerHolidayDao;
    }

    @CacheResult(cacheName = Caches.WORKER_HOLIDAYS)
    @Transactional
    public Set<LocalDate> getWorkerHolidays(final String workerExternalId) {
        LOG.debug("BEGIN getWorkerHolidays: [{}]", workerExternalId);

        List<HolidayVo> holidayVoList = workerHolidayDao.findWorkerHolidayAsHolidayVoByWorkerExternalId(workerExternalId);

        Set<LocalDate> holidays = holidayVoList.stream().map(HolidayVo::getDay).collect(Collectors.toSet());

        LOG.debug("END getWorkerHolidays");

        return holidays;
    }

    @CacheInvalidate(cacheName = Caches.WORKER_HOLIDAYS)
    public void updateWorkerHolidays(@CacheKey final String workerExternalId, final ComunytekHolidaysDto comunytekHolidaysDto) {
        LOG.debug("BEGIN updateWorkerHolidays: [{}] [{}]", workerExternalId, comunytekHolidaysDto);

        List<WorkerHoliday> workerHolidays = workerHolidayDao.findWorkerHolidayByWorkerExternalId(workerExternalId);

        // Delete holidays not declared anymore
        workerHolidays.stream()
                .filter(h -> !comunytekHolidaysDto.getHolidays().contains(h.getId().getDay()))
                .forEach(workerHolidayDao::delete);

        // Insert new declared holidays
        comunytekHolidaysDto.getHolidays().stream()
                .map(day -> {
                    WorkerHolidayId workerHolidayId = new WorkerHolidayId();
                    workerHolidayId.setDay(day);
                    workerHolidayId.setWorkerExternalId(comunytekHolidaysDto.getUsername());

                    return workerHolidayId;
                })
                .map(workerHolidayId -> {
                    WorkerHoliday workerHoliday = new WorkerHoliday();
                    workerHoliday.setId(workerHolidayId);

                    return workerHoliday;
                })
                .filter(h -> !workerHolidays.contains(h))
                .forEach(workerHolidayDao::persist);

        LOG.debug("END updateWorkerHolidays");
    }
}