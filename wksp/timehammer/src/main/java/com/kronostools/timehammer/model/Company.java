package com.kronostools.timehammer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity(name = "Company")
@Table(name = "company")
public class Company {
    @Id
    @Column(nullable = false, unique = true, insertable = false, updatable = false)
    private String code;

    @Column(nullable = false, insertable = false, updatable = false)
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company city = (Company) o;
        return code.equals(city.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}