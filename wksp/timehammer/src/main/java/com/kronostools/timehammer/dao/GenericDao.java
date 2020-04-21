package com.kronostools.timehammer.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class GenericDao {
    protected EntityManager em;

    public GenericDao() {} // dummy constructor

    @Inject
    public GenericDao(final EntityManager em) {
        this.em = em;
    }
}
