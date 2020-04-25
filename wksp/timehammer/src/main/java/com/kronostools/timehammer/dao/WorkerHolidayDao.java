package com.kronostools.timehammer.dao;

import com.kronostools.timehammer.model.Worker;
import com.kronostools.timehammer.model.WorkerHoliday;
import com.kronostools.timehammer.service.TimeMachineService;
import com.kronostools.timehammer.vo.HolidayVo;
import org.hibernate.Session;
import org.hibernate.jpa.QueryHints;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class WorkerHolidayDao extends GenericDao {
    private final TimeMachineService timeMachineService;

    public WorkerHolidayDao(final EntityManager em,
                            final TimeMachineService timeMachineService) {
        super(em);
        this.timeMachineService = timeMachineService;
    }

    public List<WorkerHoliday> fetchAllWorkerHolidayByWorkerExternalId(final String workerExternalId) {
        return em.createQuery(
                "SELECT h " +
                        "FROM WorkerHoliday h " +
                        "WHERE h.id.workerExternalId = :workerExternalId " +
                        "ORDER BY h.id.day ASC", WorkerHoliday.class)
                .setParameter("workerExternalId", workerExternalId)
                .getResultList();
    }

    public List<HolidayVo> fetchAllWorkerHolidayAsHolidayVoByWorkerExternalId(final String workerExternalId) {
        return em.createQuery(
                "SELECT new com.kronostools.timehammer.vo.HolidayVo(id.day) " +
                        "FROM WorkerHoliday " +
                        "WHERE id.workerExternalId = :workerExternalId " +
                        "ORDER BY id.day ASC", HolidayVo.class)
                .setParameter("workerExternalId", workerExternalId)
                .setHint(QueryHints.HINT_READONLY,true)
                .getResultList();
    }

    public List<HolidayVo> fetchPendingWorkerHolidayAsHolidayVoByWorkerExternalId(final String workerExternalId) {
        return em.createQuery(
                "SELECT new com.kronostools.timehammer.vo.HolidayVo(id.day) " +
                        "FROM WorkerHoliday " +
                        "WHERE id.workerExternalId = :workerExternalId " +
                        "AND id.day >= :today " +
                        "ORDER BY id.day ASC", HolidayVo.class)
                .setParameter("workerExternalId", workerExternalId)
                .setParameter("today", timeMachineService.getNow().toLocalDate())
                .setHint(QueryHints.HINT_READONLY,true)
                .getResultList();
    }

    public void persist(final WorkerHoliday workerHoliday) {
        if (workerHoliday.getWorker() == null) {
            Session session = em.unwrap(Session.class);

            Worker worker = session.load(Worker.class, workerHoliday.getId().getWorkerExternalId());

            workerHoliday.setWorker(worker);
        }

        em.persist(workerHoliday);
    }

    public void delete(final WorkerHoliday workerHoliday) {
        em.remove(workerHoliday);
    }
}
