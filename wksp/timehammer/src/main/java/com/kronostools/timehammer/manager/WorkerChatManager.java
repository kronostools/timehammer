package com.kronostools.timehammer.manager;

import com.kronostools.timehammer.dao.WorkerChatDao;
import com.kronostools.timehammer.model.WorkerChat;
import com.kronostools.timehammer.model.WorkerChatId;
import com.kronostools.timehammer.utils.Constants.Caches;
import com.kronostools.timehammer.vo.WorkerChatVo;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class WorkerChatManager {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerChatManager.class);

    private final WorkerChatDao workerChatDao;

    public WorkerChatManager(final WorkerChatDao workerChatDao) {
        this.workerChatDao = workerChatDao;
    }

    @CacheResult(cacheName = Caches.WORKER_CHATS)
    @Transactional
    public Set<String> getWorkerChatsByInternalId(final String workerInternalId) {
        LOG.debug("BEGIN getWorkerChats: [{}]", workerInternalId);

        List<WorkerChatVo> chatVoList = workerChatDao.findWorkerChatAsWorkerChatVoByWorkerInternalId(workerInternalId);

        Set<String> chats = chatVoList.stream().map(WorkerChatVo::getChatId).collect(Collectors.toSet());

        LOG.debug("END getWorkerChats");

        return chats;
    }

    @CacheInvalidate(cacheName = Caches.WORKER_CHATS)
    public void addNewChat(@CacheKey final String workerInternalId, final String chatId) {
        LOG.debug("BEGIN addNewChat: [{}] [{}]", workerInternalId, chatId);

        WorkerChatId workerChatId = new WorkerChatId();
        workerChatId.setWorkerInternalId(workerInternalId);
        workerChatId.setChatId(chatId);

        WorkerChat workerChat = new WorkerChat();
        workerChat.setId(workerChatId);

        workerChatDao.persist(workerChat);

        LOG.debug("END addNewChat");
    }

    @CacheInvalidate(cacheName = Caches.WORKER_CHATS)
    public void removeChat(@CacheKey final String workerInternalId, final String chatId) {
        LOG.debug("BEGIN removeChat: [{}] [{}]", workerInternalId, chatId);

        WorkerChatId workerChatId = new WorkerChatId();
        workerChatId.setWorkerInternalId(workerInternalId);
        workerChatId.setChatId(chatId);

        workerChatDao.delete(workerChatId);

        LOG.debug("END removeChat");
    }
}