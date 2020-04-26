package com.kronostools.timehammer.service;

import com.kronostools.timehammer.dto.CompanyDto;
import com.kronostools.timehammer.manager.CompanyManager;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CompanyService {
    private final CompanyManager companyManager;

    public CompanyService(final CompanyManager companyManager) {
        this.companyManager = companyManager;
    }

    public List<CompanyDto> getAllCompanies() {
        return companyManager.getAllCompanies();
    }

    public boolean companyByCodeExists(final String code) {
        return companyManager.companyByCodeExists(code);
    }
}