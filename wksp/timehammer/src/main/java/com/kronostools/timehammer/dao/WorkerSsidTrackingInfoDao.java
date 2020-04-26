package com.kronostools.timehammer.dao;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kronostools.timehammer.model.Worker;
import com.kronostools.timehammer.model.WorkerSsidTrackingInfo;
import com.kronostools.timehammer.service.TimeMachineService;
import com.kronostools.timehammer.utils.Constants;
import com.kronostools.timehammer.utils.Constants.Buses;
import com.kronostools.timehammer.vo.SsidTrackingInfoVo;
import io.vertx.axle.core.eventbus.EventBus;
import org.hibernate.Session;
import org.hibernate.annotations.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class WorkerSsidTrackingInfoDao extends GenericDao {
    private static final Logger LOG = LoggerFactory.getLogger(WorkerSsidTrackingInfoDao.class);

    private final TimeMachineService timeMachineService;
    private final EventBus bus;

    private Cache<String, SsidTrackingInfoVo> cache;

    public WorkerSsidTrackingInfoDao(final EntityManager em,
                                     final TimeMachineService timeMachineService,
                                     final EventBus bus) {
        super(em);
        this.timeMachineService = timeMachineService;
        this.bus = bus;
    }

    @PostConstruct
    void init() {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(8L, TimeUnit.HOURS)
                .build();
    }

    public SsidTrackingInfoVo fetchByWorkerInternalId(final String workerInternalId) {
        LOG.debug("BEGIN fetchByWorkerInternalId: {}", workerInternalId);

        final LocalDateTime todayAtMidnight = timeMachineService.getTodayAtMidnight();

        return cache.get(workerInternalId, key -> {
            LOG.debug("Worker ssid tracking info was not found in cache, getting from database ...");

            return em.createQuery(
                    "SELECT new com.kronostools.timehammer.vo.SsidTrackingInfoVo(workerExternalId, ssidReported, reported) " +
                            "FROM WorkerSsidTrackingInfo " +
                            "WHERE workerInternalId = :workerInternalId " +
                            "AND reported >= :reported " +
                            "ORDER BY reported DESC", SsidTrackingInfoVo.class)
                    .setParameter("workerInternalId", key)
                    .setParameter("reported", todayAtMidnight)
                    .setHint(QueryHints.READ_ONLY, true)
                    .setMaxResults(1)
                    .getResultStream().findFirst()
                        .orElseGet(() -> {
                            LOG.debug("Worker ssid tracking info was not found in database or it was expired (from yesterday), initializing it ...");

                            return new SsidTrackingInfoVo(key, Constants.NO_SSID, todayAtMidnight);
                        });
        });
    }

    public void updateWorkerSsidTrackingInfo(final SsidTrackingInfoVo newSsidTrackingInfoVo) {
        LOG.debug("BEGIN updateWorkerSsidTrackingInfo: [{}]", newSsidTrackingInfoVo);

        cache.put(newSsidTrackingInfoVo.getWorkerInternalId(), newSsidTrackingInfoVo);
        LOG.debug("Updated worker ssid tracking info [{}] in cache", newSsidTrackingInfoVo);

        this.updateWorkerSsidTrackingInfoAsync(newSsidTrackingInfoVo);

        LOG.debug("END updateWorkerSsidTrackingInfo");
    }

    private void updateWorkerSsidTrackingInfoAsync(final SsidTrackingInfoVo newSsidTrackingInfoVo) {
        LOG.debug("BEGIN updateWorkerSsidTrackingInfoAsync: [{}]", newSsidTrackingInfoVo);

        bus.publish(Buses.UPDATE_WORKER_SSID_TRACKING_INFO, newSsidTrackingInfoVo);
        LOG.debug("Emited event [{}] in bus '{}'", newSsidTrackingInfoVo, Buses.UPDATE_WORKER_SSID_TRACKING_INFO);

        LOG.debug("END updateWorkerSsidTrackingInfoAsync");
    }

    @Transactional
    public void update(final WorkerSsidTrackingInfo newWorkerSsidTrackingInfo) {
        LOG.debug("BEGIN update");

        Session session = em.unwrap(Session.class);

        if (newWorkerSsidTrackingInfo.getWorker() == null) {
            Worker worker = session.load(Worker.class, newWorkerSsidTrackingInfo.getWorkerInternalId());
            newWorkerSsidTrackingInfo.setWorker(worker);
        }

        session.update(newWorkerSsidTrackingInfo);

        LOG.debug("END update");
    }

    public void initializeSsidTrackingInfo(final WorkerSsidTrackingInfo workerSsidTrackingInfo) {
        if (workerSsidTrackingInfo.getWorker() == null) {
            Session session = em.unwrap(Session.class);

            Worker worker = session.load(Worker.class, workerSsidTrackingInfo.getWorkerInternalId());
            workerSsidTrackingInfo.setWorker(worker);
        }

        em.persist(workerSsidTrackingInfo);
    }
}