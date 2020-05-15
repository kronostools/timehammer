package com.kronostools.timehammer.web.dto;

public class ScheduleSummaryDto extends Dto {
    private String name;
    private String startTimestamp;
    private String endTimestamp;
    private int totalItemsProcessed;
    private int itemsProcessedOk;
    private int itemsProcessedKo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(String startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public String getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(String endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public int getTotalItemsProcessed() {
        return totalItemsProcessed;
    }

    public void setTotalItemsProcessed(int totalItemsProcessed) {
        this.totalItemsProcessed = totalItemsProcessed;
    }

    public int getItemsProcessedOk() {
        return itemsProcessedOk;
    }

    public void setItemsProcessedOk(int itemsProcessedOk) {
        this.itemsProcessedOk = itemsProcessedOk;
    }

    public int getItemsProcessedKo() {
        return itemsProcessedKo;
    }

    public void setItemsProcessedKo(int itemsProcessedKo) {
        this.itemsProcessedKo = itemsProcessedKo;
    }
}
