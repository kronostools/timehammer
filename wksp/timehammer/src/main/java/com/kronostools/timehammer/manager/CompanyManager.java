package com.kronostools.timehammer.manager;

import com.kronostools.timehammer.dao.CompanyDao;
import com.kronostools.timehammer.dto.CompanyDto;
import com.kronostools.timehammer.utils.Constants.Caches;
import io.quarkus.cache.CacheResult;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CompanyManager {
    private final CompanyDao companyDao;

    public CompanyManager(final CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @CacheResult(cacheName = Caches.COMPANY_LIST)
    @Transactional
    public List<CompanyDto> getAllCompanies() {
        return companyDao.getAllCompanies();
    }
}
