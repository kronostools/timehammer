package com.kronostools.timehammer.dao;

import com.kronostools.timehammer.dto.CityDto;
import com.kronostools.timehammer.vo.CityVo;
import org.hibernate.jpa.QueryHints;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CityDao extends GenericDao {
    public CityDao(final EntityManager em) {
        super(em);
    }

    public List<CityDto> getAllCities() {
        return em.createQuery(
                "SELECT new com.kronostools.timehammer.dto.CityDto(code, name) " +
                        "FROM City " +
                        "ORDER BY name ASC", CityDto.class)
                .setHint(QueryHints.HINT_READONLY,true)
                .getResultList();
    }

    public Optional<CityVo> findByCode(final String code) {
        return em.createQuery(
                "SELECT new com.kronostools.timehammer.vo.CityVo(code, timezone) " +
                        "FROM City " +
                        "WHERE code = :code", CityVo.class)
                .setParameter("code", code)
                .setHint(QueryHints.HINT_READONLY,true)
                .getResultStream()
                .findFirst();
    }
}