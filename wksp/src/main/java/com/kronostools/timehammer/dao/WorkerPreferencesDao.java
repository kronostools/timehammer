package com.kronostools.timehammer.dao;

import com.kronostools.timehammer.model.City;
import com.kronostools.timehammer.model.Worker;
import com.kronostools.timehammer.model.WorkerPreferences;
import com.kronostools.timehammer.vo.WorkerPreferencesVo;
import org.hibernate.Session;
import org.hibernate.annotations.QueryHints;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class WorkerPreferencesDao extends GenericDao {
    public WorkerPreferencesDao(final EntityManager em) {
        super(em);
    }

    public List<WorkerPreferencesVo> fetchAllAsWorkerPreferencesVo() {
        return em.createQuery(
                "SELECT new com.kronostools.timehammer.vo.WorkerPreferencesVo(p.workerExternalId, p.workSsid, " +
                        "p.workStartMon, p.workEndMon, p.lunchStartMon, p.lunchEndMon, " +
                        "p.workStartTue, p.workEndTue, p.lunchStartTue, p.lunchEndTue, " +
                        "p.workStartWed, p.workEndWed, p.lunchStartWed, p.lunchEndWed, " +
                        "p.workStartThu, p.workEndThu, p.lunchStartThu, p.lunchEndThu, " +
                        "p.workStartFri, p.workEndFri, p.lunchStartFri, p.lunchEndFri, " +
                        "c.code, c.timezone) " +
                        "FROM WorkerPreferences p " +
                        "JOIN p.workCity c ", WorkerPreferencesVo.class)
                .setHint(QueryHints.READ_ONLY, true)
                .getResultList();
    }

    public WorkerPreferencesVo fetchByWorkerExternalIdAsWorkerPreferencesVo(final String workerExternalId) {
        return em.createQuery(
                "SELECT new com.kronostools.timehammer.vo.WorkerPreferencesVo(p.workerExternalId, p.workSsid, " +
                        "p.workStartMon, p.workEndMon, p.lunchStartMon, p.lunchEndMon, " +
                        "p.workStartTue, p.workEndTue, p.lunchStartTue, p.lunchEndTue, " +
                        "p.workStartWed, p.workEndWed, p.lunchStartWed, p.lunchEndWed, " +
                        "p.workStartThu, p.workEndThu, p.lunchStartThu, p.lunchEndThu, " +
                        "p.workStartFri, p.workEndFri, p.lunchStartFri, p.lunchEndFri, " +
                        "c.code, c.timezone) " +
                        "FROM WorkerPreferences p " +
                        "JOIN p.workCity c " +
                        "WHERE p.workerExternalId = :workerExternalId", WorkerPreferencesVo.class)
                .setParameter("workerExternalId", workerExternalId)
                .getSingleResult();
    }

    public void registerWorkerPreferences(final String workerExternalId, final WorkerPreferencesVo workerPreferencesVo) {
        WorkerPreferences workerPreferences = new WorkerPreferences();
        workerPreferences.setWorkerExternalId(workerExternalId);
        workerPreferences.setWorkSsid(workerPreferencesVo.getWorkSsid());
        workerPreferences.setWorkStartMon(workerPreferencesVo.getWorkStartMon());
        workerPreferences.setWorkEndMon(workerPreferencesVo.getWorkEndMon());
        workerPreferences.setLunchStartMon(workerPreferencesVo.getLunchStartMon());
        workerPreferences.setLunchEndMon(workerPreferencesVo.getLunchEndMon());
        workerPreferences.setWorkStartTue(workerPreferencesVo.getWorkStartTue());
        workerPreferences.setWorkEndTue(workerPreferencesVo.getWorkEndTue());
        workerPreferences.setLunchStartTue(workerPreferencesVo.getLunchStartTue());
        workerPreferences.setLunchEndTue(workerPreferencesVo.getLunchEndTue());
        workerPreferences.setWorkStartWed(workerPreferencesVo.getWorkStartWed());
        workerPreferences.setWorkEndWed(workerPreferencesVo.getWorkEndWed());
        workerPreferences.setLunchStartWed(workerPreferencesVo.getLunchStartWed());
        workerPreferences.setLunchEndWed(workerPreferencesVo.getLunchEndWed());
        workerPreferences.setWorkStartThu(workerPreferencesVo.getWorkStartThu());
        workerPreferences.setWorkEndThu(workerPreferencesVo.getWorkEndThu());
        workerPreferences.setLunchStartThu(workerPreferencesVo.getLunchStartThu());
        workerPreferences.setLunchEndThu(workerPreferencesVo.getLunchEndThu());
        workerPreferences.setWorkStartFri(workerPreferencesVo.getWorkStartFri());
        workerPreferences.setWorkEndFri(workerPreferencesVo.getWorkEndFri());
        workerPreferences.setLunchStartFri(workerPreferencesVo.getLunchStartFri());
        workerPreferences.setLunchEndFri(workerPreferencesVo.getLunchEndFri());

        Session session = em.unwrap(Session.class);

        City workCity = session.load(City.class, workerPreferencesVo.getCityCode());
        workerPreferences.setWorkCity(workCity);

        Worker worker = session.load(Worker.class, workerExternalId);
        workerPreferences.setWorker(worker);

        em.persist(workerPreferences);
    }
}