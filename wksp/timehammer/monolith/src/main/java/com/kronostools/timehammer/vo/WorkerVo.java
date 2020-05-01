package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.enums.Profile;

public class WorkerVo {
    private final String internalId;
    private final String fullName;
    private final Profile profile;

    public WorkerVo(final String internalId, final String fullName, final Profile profile) {
        this.internalId = internalId;
        this.fullName = fullName;
        this.profile = profile;
    }

    public String getInternalId() {
        return internalId;
    }

    public String getFullName() {
        return fullName;
    }

    public Profile getProfile() {
        return profile;
    }

    @Override
    public String toString() {
        return "WorkerVo{" +
                "internalId='" + internalId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", profile='" + profile + '\'' +
                '}';
    }
}