package com.kronostools.timehammer.model;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "CityHoliday")
@Table(name = "city_holiday")
public class CityHoliday {
    @EmbeddedId
    private CityHolidayId id;

    @Column(updatable = false)
    private String description;

    @MapsId("cityCode")
    @ManyToOne(fetch = FetchType.LAZY)
    private City city;

    public CityHolidayId getId() {
        return id;
    }

    public void setId(CityHolidayId id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityHoliday that = (CityHoliday) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}