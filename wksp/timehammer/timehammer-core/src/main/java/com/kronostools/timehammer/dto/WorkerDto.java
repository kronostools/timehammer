package com.kronostools.timehammer.dto;

public class WorkerDto {
    private final String internalId;
    private final String name;

    public WorkerDto(final String internalId, final String name) {
        this.internalId = internalId;
        this.name = name;
    }

    public String getInternalId() {
        return internalId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "WorkerDto{" +
                "internalId='" + internalId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}