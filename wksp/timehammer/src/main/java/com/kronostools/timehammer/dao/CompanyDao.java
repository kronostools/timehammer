package com.kronostools.timehammer.dao;

import com.kronostools.timehammer.dto.CompanyDto;
import org.hibernate.jpa.QueryHints;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class CompanyDao extends GenericDao {
    public CompanyDao(final EntityManager em) {
        super(em);
    }

    public List<CompanyDto> getAllCompanies() {
        return em.createQuery(
                "SELECT new com.kronostools.timehammer.dto.CompanyDto(code, name) " +
                        "FROM Company " +
                        "ORDER BY name ASC", CompanyDto.class)
                .setHint(QueryHints.HINT_READONLY,true)
                .getResultList();
    }
}