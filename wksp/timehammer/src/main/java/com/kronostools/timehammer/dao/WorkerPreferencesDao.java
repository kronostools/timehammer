package com.kronostools.timehammer.dao;

import com.kronostools.timehammer.model.City;
import com.kronostools.timehammer.model.Worker;
import com.kronostools.timehammer.model.WorkerPreferences;
import com.kronostools.timehammer.vo.WorkerCurrentPreferencesVo;
import com.kronostools.timehammer.vo.WorkerPreferencesVo;
import org.hibernate.Session;
import org.hibernate.annotations.QueryHints;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class WorkerPreferencesDao extends GenericDao {
    public WorkerPreferencesDao(final EntityManager em) {
        super(em);
    }

    public List<WorkerCurrentPreferencesVo> fetchAllAsWorkerCurrentPreferencesVo(final LocalDateTime timestamp) {
        final String dayOfWeek = timestamp.toLocalDate().getDayOfWeek().name();

        return em.createQuery(
                "SELECT new com.kronostools.timehammer.vo.WorkerCurrentPreferencesVo(cast(:date as date), p.workerExternalId, p.workSsid, " +
                        "CASE WHEN :dayOfWeek = 'MONDAY' THEN p.workStartMon ELSE (CASE WHEN :dayOfWeek = 'TUESDAY' THEN p.workStartTue ELSE (CASE WHEN :dayOfWeek = 'WEDNESDAY' THEN p.workStartWed ELSE (CASE WHEN :dayOfWeek = 'THURSDAY' THEN p.workStartThu ELSE (CASE WHEN :dayOfWeek = 'FRIDAY' THEN p.workStartFri ELSE NULL END) END) END) END) END, " +
                        "CASE WHEN :dayOfWeek = 'MONDAY' THEN p.workEndMon ELSE (CASE WHEN :dayOfWeek = 'TUESDAY' THEN p.workEndTue ELSE (CASE WHEN :dayOfWeek = 'WEDNESDAY' THEN p.workEndWed ELSE (CASE WHEN :dayOfWeek = 'THURSDAY' THEN p.workEndThu ELSE (CASE WHEN :dayOfWeek = 'FRIDAY' THEN p.workEndFri ELSE NULL END) END) END) END) END, " +
                        "CASE WHEN :dayOfWeek = 'MONDAY' THEN p.lunchStartMon ELSE (CASE WHEN :dayOfWeek = 'TUESDAY' THEN p.lunchStartTue ELSE (CASE WHEN :dayOfWeek = 'WEDNESDAY' THEN p.lunchStartWed ELSE (CASE WHEN :dayOfWeek = 'THURSDAY' THEN p.lunchStartThu ELSE (CASE WHEN :dayOfWeek = 'FRIDAY' THEN p.lunchStartFri ELSE NULL END) END) END) END) END, " +
                        "CASE WHEN :dayOfWeek = 'MONDAY' THEN p.lunchEndMon ELSE (CASE WHEN :dayOfWeek = 'TUESDAY' THEN p.lunchEndTue ELSE (CASE WHEN :dayOfWeek = 'WEDNESDAY' THEN p.lunchEndWed ELSE (CASE WHEN :dayOfWeek = 'THURSDAY' THEN p.lunchEndThu ELSE (CASE WHEN :dayOfWeek = 'FRIDAY' THEN p.lunchEndFri ELSE NULL END) END) END) END) END, " +
                        "c.code, c.timezone, " +
                        "wh.id.day is not null, " +
                        "ch.id.day is not null) " +
                        "FROM WorkerPreferences p " +
                        "JOIN p.workCity c " +
                        "LEFT JOIN c.holidays ch WITH ch.id.day = :date " +
                        "LEFT JOIN WorkerHoliday wh WITH wh.id.workerExternalId = p.workerExternalId AND wh.id.day = :date ", WorkerCurrentPreferencesVo.class)
                .setParameter("date", timestamp.toLocalDate())
                .setParameter("dayOfWeek", dayOfWeek)
                .setHint(QueryHints.READ_ONLY, true)
                .getResultList();
    }

    public WorkerCurrentPreferencesVo fetchByWorkerExternalIdAsWorkerCurrentPreferencesVo(final String workerExternalId, final LocalDateTime timestamp) {
        final String dayOfWeek = timestamp.toLocalDate().getDayOfWeek().name();

        return em.createQuery(
                "SELECT new com.kronostools.timehammer.vo.WorkerCurrentPreferencesVo(cast(:date as date), p.workerExternalId, p.workSsid, " +
                        "CASE WHEN :dayOfWeek = 'MONDAY' THEN p.workStartMon ELSE (CASE WHEN :dayOfWeek = 'TUESDAY' THEN p.workStartTue ELSE (CASE WHEN :dayOfWeek = 'WEDNESDAY' THEN p.workStartWed ELSE (CASE WHEN :dayOfWeek = 'THURSDAY' THEN p.workStartThu ELSE (CASE WHEN :dayOfWeek = 'FRIDAY' THEN p.workStartFri ELSE NULL END) END) END) END) END, " +
                        "CASE WHEN :dayOfWeek = 'MONDAY' THEN p.workEndMon ELSE (CASE WHEN :dayOfWeek = 'TUESDAY' THEN p.workEndTue ELSE (CASE WHEN :dayOfWeek = 'WEDNESDAY' THEN p.workEndWed ELSE (CASE WHEN :dayOfWeek = 'THURSDAY' THEN p.workEndThu ELSE (CASE WHEN :dayOfWeek = 'FRIDAY' THEN p.workEndFri ELSE NULL END) END) END) END) END, " +
                        "CASE WHEN :dayOfWeek = 'MONDAY' THEN p.lunchStartMon ELSE (CASE WHEN :dayOfWeek = 'TUESDAY' THEN p.lunchStartTue ELSE (CASE WHEN :dayOfWeek = 'WEDNESDAY' THEN p.lunchStartWed ELSE (CASE WHEN :dayOfWeek = 'THURSDAY' THEN p.lunchStartThu ELSE (CASE WHEN :dayOfWeek = 'FRIDAY' THEN p.lunchStartFri ELSE NULL END) END) END) END) END, " +
                        "CASE WHEN :dayOfWeek = 'MONDAY' THEN p.lunchEndMon ELSE (CASE WHEN :dayOfWeek = 'TUESDAY' THEN p.lunchEndTue ELSE (CASE WHEN :dayOfWeek = 'WEDNESDAY' THEN p.lunchEndWed ELSE (CASE WHEN :dayOfWeek = 'THURSDAY' THEN p.lunchEndThu ELSE (CASE WHEN :dayOfWeek = 'FRIDAY' THEN p.lunchEndFri ELSE NULL END) END) END) END) END, " +
                        "c.code, c.timezone, " +
                        "wh.id.day is not null, " +
                        "ch.id.day is not null) " +
                        "FROM WorkerPreferences p " +
                        "JOIN p.workCity c " +
                        "LEFT JOIN c.holidays ch WITH ch.id.day = :date " +
                        "LEFT JOIN WorkerHoliday wh WITH wh.id.workerExternalId = p.workerExternalId AND wh.id.day = :date " +
                        "WHERE p.workerExternalId = :workerExternalId", WorkerCurrentPreferencesVo.class)
                .setParameter("workerExternalId", workerExternalId)
                .setParameter("date", timestamp.toLocalDate())
                .setParameter("dayOfWeek", dayOfWeek)
                .setHint(QueryHints.READ_ONLY, true)
                .getSingleResult();
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
        workerPreferences.setWorkStartMon(workerPreferencesVo.getZonedWorkStartMon());
        workerPreferences.setWorkEndMon(workerPreferencesVo.getZonedWorkEndMon());
        workerPreferences.setLunchStartMon(workerPreferencesVo.getZonedLunchStartMon());
        workerPreferences.setLunchEndMon(workerPreferencesVo.getZonedLunchEndMon());
        workerPreferences.setWorkStartTue(workerPreferencesVo.getZonedWorkStartTue());
        workerPreferences.setWorkEndTue(workerPreferencesVo.getZonedWorkEndTue());
        workerPreferences.setLunchStartTue(workerPreferencesVo.getZonedLunchStartTue());
        workerPreferences.setLunchEndTue(workerPreferencesVo.getZonedLunchEndTue());
        workerPreferences.setWorkStartWed(workerPreferencesVo.getZonedWorkStartWed());
        workerPreferences.setWorkEndWed(workerPreferencesVo.getZonedWorkEndWed());
        workerPreferences.setLunchStartWed(workerPreferencesVo.getZonedLunchStartWed());
        workerPreferences.setLunchEndWed(workerPreferencesVo.getZonedLunchEndWed());
        workerPreferences.setWorkStartThu(workerPreferencesVo.getZonedWorkStartThu());
        workerPreferences.setWorkEndThu(workerPreferencesVo.getZonedWorkEndThu());
        workerPreferences.setLunchStartThu(workerPreferencesVo.getZonedLunchStartThu());
        workerPreferences.setLunchEndThu(workerPreferencesVo.getZonedLunchEndThu());
        workerPreferences.setWorkStartFri(workerPreferencesVo.getZonedWorkStartFri());
        workerPreferences.setWorkEndFri(workerPreferencesVo.getZonedWorkEndFri());
        workerPreferences.setLunchStartFri(workerPreferencesVo.getZonedLunchStartFri());
        workerPreferences.setLunchEndFri(workerPreferencesVo.getZonedLunchEndFri());

        Session session = em.unwrap(Session.class);

        City workCity = session.load(City.class, workerPreferencesVo.getCityCode());
        workerPreferences.setWorkCity(workCity);

        Worker worker = session.load(Worker.class, workerExternalId);
        workerPreferences.setWorker(worker);

        em.persist(workerPreferences);
    }
}