package com.kronostools.timehammer.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class CityHolidayId implements Serializable {
    @Column(name = "city_code", nullable = false, updatable = false)
    private String cityCode;

    @Column(nullable = false, updatable = false)
    private LocalDate day;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityHolidayId that = (CityHolidayId) o;
        return cityCode.equals(that.cityCode) &&
                day.equals(that.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityCode, day);
    }
}
