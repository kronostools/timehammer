package com.kronostools.timehammer.vo;

import com.kronostools.timehammer.enums.Profile;

public class WorkerAndPreferencesVo {
    private final String registrationId;
    private final String externalId;
    private final String externalPassword;
    private final String fullName;
    private final Profile profile;
    private final WorkerPreferencesVo preferences;

    public WorkerAndPreferencesVo(final WorkerVo workerVo, final WorkerPreferencesVo preferences) {
        this.registrationId = workerVo.getRegistrationId();
        this.externalId = workerVo.getExternalId();
        this.externalPassword = workerVo.getExternalPassword();
        this.fullName = workerVo.getFullName();
        this.profile = workerVo.getProfile();
        this.preferences = preferences;
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

    public WorkerPreferencesVo getPreferences() {
        return preferences;
    }

    @Override
    public String toString() {
        return "WorkerAndPreferencesVo{" +
                "registrationId='" + registrationId + '\'' +
                ", externalId='" + externalId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", profile='" + profile.name() + '\'' +
                ", preferences=" + preferences +
                '}';
    }
}
