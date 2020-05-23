package com.kronostools.timehammer.common.config;

import java.util.concurrent.TimeUnit;

public class ExpirationConfig {
    private Integer qty;
    private TimeUnit unit;

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }
}