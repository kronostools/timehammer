package com.kronostools.timehammer.dao;

import com.kronostools.timehammer.model.Worker;
import com.kronostools.timehammer.model.WorkerSsidTrackingEvent;
import com.kronostools.timehammer.vo.SsidTrackingEventVo;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SsidTrackingEventDao extends GenericDao {
    private static final Logger LOG = LoggerFactory.getLogger(SsidTrackingEventDao.class);

    public SsidTrackingEventDao(final EntityManager em) {
        super(em);
    }

    public int cleanSsidTrackingEventsUntil(final LocalDateTime timestamp) {
        return em.createQuery("DELETE FROM WorkerSsidTrackingEvent WHERE occurred < :timestamp")
                .setParameter("timestamp", timestamp)
                .executeUpdate();
    }

    public List<SsidTrackingEventVo> getSsidTrackingEventsBetween(final LocalDateTime begin, final LocalDateTime end) {
        return em.createQuery(
                "SELECT e " +
                        "FROM new com.kronostools.timehammer.vo.SsidTrackingEventVo(workerExternalId, eventType, occurred) " +
                        "WHERE occurred >= :begin " +
                        "AND occurred < :end", SsidTrackingEventVo.class)
                .setParameter("begin", begin)
                .setParameter("end", end)
                .getResultList();
    }

    public void save(final WorkerSsidTrackingEvent workerSsidTrackingEvent) {
        LOG.debug("BEGIN save");

        Session session = em.unwrap(Session.class);

        if (workerSsidTrackingEvent.getWorker() == null) {
            Worker worker = session.load(Worker.class, workerSsidTrackingEvent.getId().getWorkerInternalId());

            workerSsidTrackingEvent.setWorker(worker);
        }

        session.save(workerSsidTrackingEvent);

        LOG.debug("END save");
    }
}