package com.kronostools.timehammer.service;

import com.kronostools.timehammer.dto.CompanyDto;
import com.kronostools.timehammer.enums.Company;

import javax.enterprise.context.ApplicationScoped;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class CompanyService {
    public Set<CompanyDto> getAllCompanies() {
        return Stream.of(Company.values())
                .filter(Company::isSelectionable)
                .map(c -> new CompanyDto(c.getCode(), c.getText()))
                .collect(Collectors.toSet());
    }
}