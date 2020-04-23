package com.kronostools.timehammer.dao;

import com.kronostools.timehammer.service.TimeMachineService;
import com.kronostools.timehammer.vo.HolidayVo;
import org.hibernate.jpa.QueryHints;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class CityHolidayDao extends GenericDao {
    private final TimeMachineService timeMachineService;

    public CityHolidayDao(final EntityManager em,
                          final TimeMachineService timeMachineService) {
        super(em);
        this.timeMachineService = timeMachineService;
    }

    public List<HolidayVo> fetchAllCityHolidayAsHolidayVoByCityCode(final String cityCode) {
        return em.createQuery(
                "SELECT new com.kronostools.timehammer.vo.HolidayVo(id.day) " +
                        "FROM CityHoliday " +
                        "WHERE id.cityCode = :cityCode", HolidayVo.class)
                .setParameter("cityCode", cityCode)
                .setHint(QueryHints.HINT_READONLY,true)
                .getResultList();
    }

    public List<HolidayVo> fetchPendingCityHolidayAsHolidayVoByCityCode(final String cityCode) {
        return em.createQuery(
                "SELECT new com.kronostools.timehammer.vo.HolidayVo(id.day) " +
                        "FROM CityHoliday " +
                        "WHERE id.cityCode = :cityCode " +
                        "AND id.day >= :today", HolidayVo.class)
                .setParameter("cityCode", cityCode)
                .setParameter("today", timeMachineService.getNow().toLocalDate())
                .setHint(QueryHints.HINT_READONLY,true)
                .getResultList();
    }
}