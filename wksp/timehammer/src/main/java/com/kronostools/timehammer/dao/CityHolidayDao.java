package com.kronostools.timehammer.dao;

import com.kronostools.timehammer.vo.HolidayVo;
import org.hibernate.jpa.QueryHints;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class CityHolidayDao extends GenericDao {
    public CityHolidayDao(final EntityManager em) {
        super(em);
    }

    public List<HolidayVo> findCityHolidayAsHolidayVoByCityCode(final String cityCode) {
        return em.createQuery(
                "SELECT new com.kronostools.timehammer.vo.HolidayVo(id.day) " +
                        "FROM CityHoliday " +
                        "WHERE id.cityCode = :cityCode ", HolidayVo.class)
                .setParameter("cityCode", cityCode)
                .setHint(QueryHints.HINT_READONLY,true)
                .getResultList();
    }
}