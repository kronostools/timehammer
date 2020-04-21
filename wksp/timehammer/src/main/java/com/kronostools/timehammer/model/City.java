package com.kronostools.timehammer.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "City")
@Table(name = "city")
public class City {
    @Id
    @Column(nullable = false, unique = true, insertable = false, updatable = false)
    private String code;

    @Column(nullable = false, insertable = false, updatable = false)
    private String name;

    @Column(nullable = false, insertable = false, updatable = false)
    private String timezone;

    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CityHoliday> holidays = new HashSet<>();

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

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Set<CityHoliday> getHolidays() {
        return holidays;
    }

    public void setHolidays(Set<CityHoliday> holidays) {
        this.holidays = holidays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return code.equals(city.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}