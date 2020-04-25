package com.kronostools.timehammer.model;

import com.kronostools.timehammer.enums.Profile;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "Worker")
@Table(name = "worker")
public class Worker {
    @Column(name = "registration_id", nullable = false, unique = true, updatable = false)
    private String registrationId;

    @Id
    @Column(name = "external_id", nullable = false, unique = true, updatable = false)
    private String externalId;

    @Column(name = "external_password", nullable = false)
    private String externalPassword;

    @Column(name = "full_name")
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Profile profile;

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getExternalPassword() {
        return externalPassword;
    }

    public void setExternalPassword(String externalPassword) {
        this.externalPassword = externalPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return externalId.equals(worker.externalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(externalId);
    }
}