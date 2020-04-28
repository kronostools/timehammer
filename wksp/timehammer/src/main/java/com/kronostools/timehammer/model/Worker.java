package com.kronostools.timehammer.model;

import com.kronostools.timehammer.enums.Profile;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "Worker")
@Table(name = "worker")
public class Worker {
    @Id
    @Column(name = "internal_id", nullable = false, unique = true, updatable = false)
    private String internalId;

    @Column(name = "full_name")
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Profile profile;

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
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
        return internalId.equals(worker.internalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(internalId);
    }
}