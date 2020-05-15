package com.kronostools.timehammer.web.dto;

public class ScheduleSummaryDtoBuilder {
    private String name;
    private String startTimestamp;
    private String endTimestamp;
    private int totalItemsProcessed;
    private int itemsProcessedOk;
    private int itemsProcessedKo;

    public ScheduleSummaryDtoBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public ScheduleSummaryDtoBuilder startTimestamp(final String startTimestamp) {
        this.startTimestamp = startTimestamp;
        return this;
    }

    public ScheduleSummaryDtoBuilder endTimestamp(final String endTimestamp) {
        this.endTimestamp = endTimestamp;
        return this;
    }

    public ScheduleSummaryDtoBuilder totalItemsProcessed(final int totalItemsProcessed) {
        this.totalItemsProcessed = totalItemsProcessed;
        return this;
    }

    public ScheduleSummaryDtoBuilder itemsProcessedOk(final int itemsProcessedOk) {
        this.itemsProcessedOk = itemsProcessedOk;
        return this;
    }

    public ScheduleSummaryDtoBuilder itemsProcessedKo(final int itemsProcessedKo) {
        this.itemsProcessedKo = itemsProcessedKo;
        return this;
    }

    public ScheduleSummaryDto build() {
        final ScheduleSummaryDto result = new ScheduleSummaryDto();
        result.setName(name);
        result.setStartTimestamp(startTimestamp);
        result.setEndTimestamp(endTimestamp);
        result.setTotalItemsProcessed(totalItemsProcessed);
        result.setItemsProcessedOk(itemsProcessedOk);
        result.setItemsProcessedKo(itemsProcessedKo);

        return result;
    }
}