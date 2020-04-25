package com.kronostools.timehammer.dao;

import com.kronostools.timehammer.enums.TrashMessageStatus;
import com.kronostools.timehammer.model.TrashMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;

@ApplicationScoped
public class TrashMessageDao extends GenericDao {
    private static final Logger LOG = LoggerFactory.getLogger(TrashMessageDao.class);

    public TrashMessageDao(final EntityManager em) {
        super(em);
    }

    public int cleanTrashMessages() {
        return em.createQuery("DELETE FROM TrashMessage WHERE status = :statusa OR status = :statusb")
                .setParameter("statusa", TrashMessageStatus.DISCARDED)
                .executeUpdate();
    }

    public void save(final TrashMessage trashMessage) {
        LOG.debug("BEGIN save");

        em.persist(trashMessage);

        LOG.debug("END save");
    }
}