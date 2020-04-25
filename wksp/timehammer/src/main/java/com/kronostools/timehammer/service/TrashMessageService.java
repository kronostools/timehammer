package com.kronostools.timehammer.service;

import com.kronostools.timehammer.dao.TrashMessageDao;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class TrashMessageService {
    private final TrashMessageDao trashMessageDao;

    public TrashMessageService(final TrashMessageDao trashMessageDao) {
        this.trashMessageDao = trashMessageDao;
    }

    @Transactional
    public void cleanTrashMessages() {
        trashMessageDao.cleanTrashMessages();
    }
}