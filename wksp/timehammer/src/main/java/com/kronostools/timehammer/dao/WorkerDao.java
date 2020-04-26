package com.kronostools.timehammer.dao;

import com.kronostools.timehammer.model.Worker;
import com.kronostools.timehammer.vo.WorkerVo;
import org.hibernate.annotations.QueryHints;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class WorkerDao extends GenericDao {
    public WorkerDao(final EntityManager em) {
        super(em);
    }

    public List<WorkerVo> fetchAllAsWorkerVo() {
        return em.createQuery(
                "SELECT new com.kronostools.timehammer.vo.WorkerVo(w.internalId, w.externalPassword, w.fullName, w.profile) " +
                        "FROM Worker w ", WorkerVo.class)
                .setHint(QueryHints.READ_ONLY, true)
                .getResultList();
    }

    public WorkerVo fetchByInternalIdAsWorkerVo(final String internalId) {
        return em.createQuery(
                "SELECT new com.kronostools.timehammer.vo.WorkerVo(w.internalId, w.externalPassword, w.fullName, w.profile) " +
                        "FROM Worker w " +
                        "WHERE internalId = :internalId", WorkerVo.class)
                .setParameter("internalId", internalId)
                .setHint(QueryHints.READ_ONLY, true)
                .getSingleResult();
    }

    public Optional<WorkerVo> fetchByChatIdAsWorkerVo(final String chatId) {
        return em.createQuery(
                "SELECT new com.kronostools.timehammer.vo.WorkerVo(w.internalId, w.externalPassword, w.fullName, w.profile) " +
                        "FROM WorkerChat wc " +
                        "JOIN wc.worker w " +
                        "WHERE wc.id.chatId = :chatId", WorkerVo.class)
                .setParameter("chatId", chatId)
                .setHint(QueryHints.READ_ONLY, true)
                .getResultStream().findFirst();
    }

    public Boolean workerAlreadyExists(final String internalId) {
        Long workerCount = em.createQuery(
                "SELECT count(w) " +
                        "FROM Worker w " +
                        "WHERE internalId = :internalId", Long.class)
                .setParameter("internalId", internalId)
                .getSingleResult();

        return workerCount > 0;
    }

    public void registerWorker(final WorkerVo workerVo) {
        Worker worker = new Worker();
        worker.setInternalId(workerVo.getInternalId());
        worker.setExternalPassword(workerVo.getExternalPassword());
        worker.setFullName(workerVo.getFullName());
        worker.setProfile(workerVo.getProfile());

        em.persist(worker);
    }
}