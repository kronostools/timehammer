package com.kronostools.timehammer.manager;

import com.kronostools.timehammer.dao.WorkerPreferencesDao;
import com.kronostools.timehammer.utils.Constants.Caches;
import com.kronostools.timehammer.vo.WorkerPreferencesVo;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class WorkerPreferencesManager {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerPreferencesManager.class);

    private final WorkerPreferencesDao workerPreferencesDao;

    public WorkerPreferencesManager(final WorkerPreferencesDao workerPreferencesDao) {
        this.workerPreferencesDao = workerPreferencesDao;
    }

    @CacheResult(cacheName = Caches.ALL_WORKER_PREFERENCES)
    @Transactional
    public List<WorkerPreferencesVo> getAllWorkersPreferences() {
        LOG.debug("BEGIN getAllWorkersPreferences");

        List<WorkerPreferencesVo> workerVoList = workerPreferencesDao.fetchAllAsWorkerPreferencesVo();

        LOG.debug("END getAllWorkersPreferences");

        return workerVoList;
    }

    @CacheResult(cacheName = Caches.WORKER_PREFERENCES)
    @Transactional
    public WorkerPreferencesVo getWorkerPreferences(final String workerExternalId) {
        LOG.debug("BEGIN getWorkerPreferences: [{}]", workerExternalId);

        WorkerPreferencesVo preferencesVo = workerPreferencesDao.fetchByWorkerExternalIdAsWorkerPreferencesVo(workerExternalId);

        LOG.debug("END getWorkerPreferences");

        return preferencesVo;
    }

    @CacheInvalidateAll(cacheName = Caches.ALL_WORKER_PREFERENCES)
    public void registerWorkerPreferences(final String workerExternalId, final WorkerPreferencesVo workerPreferencesVo) {
        LOG.debug("BEGIN registerWorkerPreferences: [{}] [{}]", workerExternalId, workerPreferencesVo);

        workerPreferencesDao.registerWorkerPreferences(workerExternalId, workerPreferencesVo);

        LOG.debug("END registerWorkerPreferences");
    }
}