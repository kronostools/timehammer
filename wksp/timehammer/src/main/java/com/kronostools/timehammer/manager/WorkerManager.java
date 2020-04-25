package com.kronostools.timehammer.manager;

import com.kronostools.timehammer.dao.WorkerDao;
import com.kronostools.timehammer.utils.Constants.Caches;
import com.kronostools.timehammer.vo.WorkerChatVo;
import com.kronostools.timehammer.vo.WorkerPreferencesVo;
import com.kronostools.timehammer.vo.WorkerVo;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class WorkerManager {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerManager.class);

    private final WorkerDao workerDao;
    private final WorkerPreferencesManager workerPreferencesManager;
    private final WorkerChatManager workerChatManager;
    private final WorkerSsidTrackingInfoManager workerSsidTrackingInfoManager;

    public WorkerManager(final WorkerDao workerDao,
                         final WorkerPreferencesManager workerPreferencesManager,
                         final WorkerChatManager workerChatManager,
                         final WorkerSsidTrackingInfoManager workerSsidTrackingInfoManager) {
        this.workerDao = workerDao;
        this.workerPreferencesManager = workerPreferencesManager;
        this.workerChatManager = workerChatManager;
        this.workerSsidTrackingInfoManager = workerSsidTrackingInfoManager;
    }

    public List<WorkerVo> getAllWorkers() {
        LOG.debug("BEGIN getAllWorkers");

        List<WorkerVo> workerVoList = workerDao.fetchAllAsWorkerVo();

        LOG.debug("END getAllWorkers");

        return workerVoList;
    }

    @CacheResult(cacheName = Caches.WORKER_BY_EXTERNAL_ID_AS_WORKERVO)
    @Transactional
    public WorkerVo getWorkerByExternalId(final String externalId) {
        LOG.debug("BEGIN getWorkerByExternalId: [{}]", externalId);

        WorkerVo workerVo = workerDao.fetchByExternalIdAsWorkerVo(externalId);

        LOG.debug("END getWorkerByExternalId");

        return workerVo;
    }

    public Optional<WorkerVo> getWorkerByChatId(final String chatId) {
        LOG.debug("BEGIN getWorkerByChatId: [{}]", chatId);

        Optional<WorkerVo> workerVo = workerDao.fetchByChatIdAsWorkerVo(chatId);

        LOG.debug("END getWorkerByChatId");

        return workerVo;
    }

    @CacheInvalidateAll(cacheName = Caches.ALL_WORKERS_CURRENT_PREFERENCES)
    public void registerWorker(final WorkerVo workerVo, final WorkerPreferencesVo workerPreferencesVo, final WorkerChatVo workerChatVo) {
        if (workerDao.workerAlreadyExists(workerVo.getExternalId())) {
            workerChatManager.addNewChat(workerVo.getExternalId(), workerChatVo.getChatId());
        } else {
            workerDao.registerWorker(workerVo);
            workerPreferencesManager.registerWorkerPreferences(workerVo.getExternalId(), workerPreferencesVo);
            workerChatManager.addNewChat(workerVo.getExternalId(), workerChatVo.getChatId());
            workerSsidTrackingInfoManager.initializeSsidTrackingInfo(workerVo.getExternalId());
        }
    }
}