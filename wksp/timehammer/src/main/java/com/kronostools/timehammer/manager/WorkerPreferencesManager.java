package com.kronostools.timehammer.manager;

import com.kronostools.timehammer.dao.WorkerPreferencesDao;
import com.kronostools.timehammer.service.TimeMachineService;
import com.kronostools.timehammer.utils.Constants.Caches;
import com.kronostools.timehammer.vo.WorkerCurrentPreferencesVo;
import com.kronostools.timehammer.vo.WorkerPreferencesVo;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class WorkerPreferencesManager {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerPreferencesManager.class);

    private final WorkerPreferencesDao workerPreferencesDao;

    public WorkerPreferencesManager(final WorkerPreferencesDao workerPreferencesDao) {
        this.workerPreferencesDao = workerPreferencesDao;
    }

    @CacheResult(cacheName = Caches.ALL_WORKERS_CURRENT_PREFERENCES)
    @Transactional
    public List<WorkerCurrentPreferencesVo> getAllWorkersCurrentPreferences(final LocalDateTime timestamp) {
        LOG.debug("BEGIN getAllWorkersCurrentPreferences: [{}]", TimeMachineService.formatDateTimeFull(timestamp));

        List<WorkerCurrentPreferencesVo> allWorkersPreferences = workerPreferencesDao.fetchAllAsWorkerCurrentPreferencesVo(timestamp);

        LOG.debug("END getAllWorkersCurrentPreferences");

        return allWorkersPreferences;
    }

    @CacheResult(cacheName = Caches.ALL_WORKERS_PREFERENCES)
    @Transactional
    public List<WorkerPreferencesVo> getAllWorkersPreferences() {
        LOG.debug("BEGIN getAllWorkersPreferences");

        List<WorkerPreferencesVo> allWorkersPreferences = workerPreferencesDao.fetchAllAsWorkerPreferencesVo();

        LOG.debug("END getAllWorkersPreferences");

        return allWorkersPreferences;
    }

    @CacheResult(cacheName = Caches.WORKER_PREFERENCES)
    @Transactional
    public WorkerPreferencesVo getWorkerPreferencesByInternalId(final String workerInternalId) {
        LOG.debug("BEGIN getWorkerPreferencesByInternalId: [{}]", workerInternalId);

        final WorkerPreferencesVo workerPreferences = workerPreferencesDao.fetchByWorkerInternalIdAsWorkerPreferencesVo(workerInternalId);

        LOG.debug("END getWorkerPreferencesByInternalId");

        return workerPreferences;
    }

    @CacheResult(cacheName = Caches.WORKER_CURRENT_PREFERENCES)
    @Transactional
    public WorkerCurrentPreferencesVo getWorkerCurrentPreferencesByInternalId(@CacheKey final String workerInternalId, final LocalDateTime timestamp) {
        LOG.debug("BEGIN getWorkerCurrentPreferencesByInternalId: [{}] [{}]", workerInternalId, TimeMachineService.formatDateTimeFull(timestamp));

        final WorkerCurrentPreferencesVo workerCurrentPreferences = workerPreferencesDao.fetchByWorkerInternalIdAsWorkerCurrentPreferencesVo(workerInternalId, timestamp);

        LOG.debug("END getWorkerCurrentPreferencesByInternalId");

        return workerCurrentPreferences;
    }

    @CacheInvalidateAll(cacheName = Caches.ALL_WORKERS_CURRENT_PREFERENCES)
    @CacheInvalidate(cacheName = Caches.WORKER_PREFERENCES)
    @CacheInvalidate(cacheName = Caches.WORKER_CURRENT_PREFERENCES)
    public void registerWorkerPreferences(@CacheKey final String workerInternalId, final WorkerPreferencesVo workerPreferencesVo) {
        LOG.debug("BEGIN registerWorkerPreferences: [{}] [{}]", workerInternalId, workerPreferencesVo);

        workerPreferencesDao.registerWorkerPreferences(workerPreferencesVo);

        LOG.debug("END registerWorkerPreferences");
    }
}