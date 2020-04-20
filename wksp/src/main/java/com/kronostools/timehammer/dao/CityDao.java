package com.kronostools.timehammer.dao;

import com.kronostools.timehammer.dto.CityDto;
import org.hibernate.jpa.QueryHints;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class CityDao extends GenericDao {
    public CityDao(final EntityManager em) {
        super(em);
    }

    public List<CityDto> getAllCities() {
        return em.createQuery(
                "SELECT new com.kronostools.timehammer.dto.CityDto(code, name) " +
                        "FROM City", CityDto.class)
                .setHint(QueryHints.HINT_READONLY,true)
                .getResultList();
    }
}