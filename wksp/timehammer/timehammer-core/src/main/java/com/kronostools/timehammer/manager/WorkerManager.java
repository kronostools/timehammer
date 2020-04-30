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

    @CacheResult(cacheName = Caches.WORKER_BY_INTERNAL_ID_AS_WORKERVO)
    @Transactional
    public WorkerVo getWorkerByInternalId(final String internalId) {
        LOG.debug("BEGIN getWorkerByInternalId: [{}]", internalId);

        WorkerVo workerVo = workerDao.fetchByInternalIdAsWorkerVo(internalId);

        LOG.debug("END getWorkerByInternalId");

        return workerVo;
    }

    public Optional<WorkerVo> findWorkerByChatId(final String chatId) {
        LOG.debug("BEGIN findWorkerByChatId: [{}]", chatId);

        Optional<WorkerVo> workerVo = workerDao.findByChatIdAsWorkerVo(chatId);

        LOG.debug("END findWorkerByChatId");

        return workerVo;
    }

    @CacheInvalidateAll(cacheName = Caches.ALL_WORKERS_CURRENT_PREFERENCES)
    public void registerWorker(final WorkerVo workerVo, final WorkerPreferencesVo workerPreferencesVo, final WorkerChatVo workerChatVo) {
        if (workerDao.workerAlreadyExists(workerVo.getInternalId())) {
            workerChatManager.addNewChat(workerVo.getInternalId(), workerChatVo.getChatId());
        } else {
            workerDao.registerWorker(workerVo);
            workerPreferencesManager.registerWorkerPreferences(workerVo.getInternalId(), workerPreferencesVo);
            workerChatManager.addNewChat(workerVo.getInternalId(), workerChatVo.getChatId());
            workerSsidTrackingInfoManager.initializeSsidTrackingInfo(workerVo.getInternalId());
        }
    }
}