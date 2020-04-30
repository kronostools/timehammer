package com.kronostools.timehammer.dao;

import com.kronostools.timehammer.model.Worker;
import com.kronostools.timehammer.model.WorkerChat;
import com.kronostools.timehammer.model.WorkerChatId;
import com.kronostools.timehammer.vo.WorkerChatVo;
import org.hibernate.Session;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class WorkerChatDao extends GenericDao {
    public WorkerChatDao(final EntityManager em) {
        super(em);
    }

    public List<WorkerChatVo> findWorkerChatAsWorkerChatVoByWorkerInternalId(final String workerInternalId) {
        return em.createQuery(
                "SELECT new com.kronostools.timehammer.vo.WorkerChatVo(id.chatId) " +
                        "FROM WorkerChat " +
                        "WHERE id.workerInternalId = :workerInternalId ", WorkerChatVo.class)
                .setParameter("workerInternalId", workerInternalId)
                .getResultList();
    }

    public void persist(final WorkerChat workerChat) {
        if (workerChat.getWorker() == null) {
            Session session = em.unwrap(Session.class);

            Worker worker = session.load(Worker.class, workerChat.getId().getWorkerInternalId());

            workerChat.setWorker(worker);
        }

        em.persist(workerChat);
    }

    public void delete(final WorkerChatId workerChatId) {
        Session session = em.unwrap(Session.class);
        WorkerChat workerChat = session.load(WorkerChat.class, workerChatId);
        em.remove(workerChat);
    }
}