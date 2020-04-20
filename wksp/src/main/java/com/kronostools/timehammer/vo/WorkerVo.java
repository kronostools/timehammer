package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.enums.Profile;

public class WorkerVo {
    private final String registrationId;
    private final String externalId;
    private final String externalPassword;
    private final String fullName;
    private final Profile profile;

    public WorkerVo(final String registrationId, final String externalId, final String externalPassword, final String fullName, final Profile profile) {
        this.registrationId = registrationId;
        this.externalId = externalId;
        this.externalPassword = externalPassword;
        this.fullName = fullName;
        this.profile = profile;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getExternalPassword() {
        return externalPassword;
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
                "registrationId='" + registrationId + '\'' +
                ", externalId='" + externalId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", profile='" + profile.name() + '\'' +
                '}';
    }
}
