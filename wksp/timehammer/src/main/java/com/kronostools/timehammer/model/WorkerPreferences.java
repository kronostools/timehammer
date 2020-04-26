package com.kronostools.timehammer.model;

import com.kronostools.timehammer.enums.Company;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Objects;

@Entity(name = "WorkerPreferences")
@Table(name = "worker_preferences")
public class WorkerPreferences {
    @Id
    @Column(name = "worker_external_id", nullable = false, unique = true, updatable = false)
    private String workerExternalId;

    @Column(name = "work_ssid", nullable = false)
    private String workSsid;

    @Column(name = "work_start_mon", nullable = false)
    private LocalTime workStartMon;
    @Column(name = "work_end_mon", nullable = false)
    private LocalTime workEndMon;
    @Column(name = "lunch_start_mon", nullable = false)
    private LocalTime lunchStartMon;
    @Column(name = "lunch_end_mon", nullable = false)
    private LocalTime lunchEndMon;

    @Column(name = "work_start_tue", nullable = false)
    private LocalTime workStartTue;
    @Column(name = "work_end_tue", nullable = false)
    private LocalTime workEndTue;
    @Column(name = "lunch_start_tue", nullable = false)
    private LocalTime lunchStartTue;
    @Column(name = "lunch_end_tue", nullable = false)
    private LocalTime lunchEndTue;

    @Column(name = "work_start_wed", nullable = false)
    private LocalTime workStartWed;
    @Column(name = "work_end_wed", nullable = false)
    private LocalTime workEndWed;
    @Column(name = "lunch_start_wed", nullable = false)
    private LocalTime lunchStartWed;
    @Column(name = "lunch_end_wed", nullable = false)
    private LocalTime lunchEndWed;

    @Column(name = "work_start_thu", nullable = false)
    private LocalTime workStartThu;
    @Column(name = "work_end_thu", nullable = false)
    private LocalTime workEndThu;
    @Column(name = "lunch_start_thu", nullable = false)
    private LocalTime lunchStartThu;
    @Column(name = "lunch_end_thu", nullable = false)
    private LocalTime lunchEndThu;

    @Column(name = "work_start_fri", nullable = false)
    private LocalTime workStartFri;
    @Column(name = "work_end_fri", nullable = false)
    private LocalTime workEndFri;
    @Column(name = "lunch_start_fri")
    private LocalTime lunchStartFri;
    @Column(name = "lunch_end_fri")
    private LocalTime lunchEndFri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_city_code")
    private City workCity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Company company;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private Worker worker;

    public String getWorkerExternalId() {
        return workerExternalId;
    }

    public void setWorkerExternalId(String workerExternalId) {
        this.workerExternalId = workerExternalId;
    }

    public String getWorkSsid() {
        return workSsid;
    }

    public void setWorkSsid(String workSsid) {
        this.workSsid = workSsid;
    }

    public LocalTime getWorkStartMon() {
        return workStartMon;
    }

    public void setWorkStartMon(LocalTime workStartMon) {
        this.workStartMon = workStartMon;
    }

    public LocalTime getWorkEndMon() {
        return workEndMon;
    }

    public void setWorkEndMon(LocalTime workEndMon) {
        this.workEndMon = workEndMon;
    }

    public LocalTime getLunchStartMon() {
        return lunchStartMon;
    }

    public void setLunchStartMon(LocalTime lunchStartMon) {
        this.lunchStartMon = lunchStartMon;
    }

    public LocalTime getLunchEndMon() {
        return lunchEndMon;
    }

    public void setLunchEndMon(LocalTime lunchEndMon) {
        this.lunchEndMon = lunchEndMon;
    }

    public LocalTime getWorkStartTue() {
        return workStartTue;
    }

    public void setWorkStartTue(LocalTime workStartTue) {
        this.workStartTue = workStartTue;
    }

    public LocalTime getWorkEndTue() {
        return workEndTue;
    }

    public void setWorkEndTue(LocalTime workEndTue) {
        this.workEndTue = workEndTue;
    }

    public LocalTime getLunchStartTue() {
        return lunchStartTue;
    }

    public void setLunchStartTue(LocalTime lunchStartTue) {
        this.lunchStartTue = lunchStartTue;
    }

    public LocalTime getLunchEndTue() {
        return lunchEndTue;
    }

    public void setLunchEndTue(LocalTime lunchEndTue) {
        this.lunchEndTue = lunchEndTue;
    }

    public LocalTime getWorkStartWed() {
        return workStartWed;
    }

    public void setWorkStartWed(LocalTime workStartWed) {
        this.workStartWed = workStartWed;
    }

    public LocalTime getWorkEndWed() {
        return workEndWed;
    }

    public void setWorkEndWed(LocalTime workEndWed) {
        this.workEndWed = workEndWed;
    }

    public LocalTime getLunchStartWed() {
        return lunchStartWed;
    }

    public void setLunchStartWed(LocalTime lunchStartWed) {
        this.lunchStartWed = lunchStartWed;
    }

    public LocalTime getLunchEndWed() {
        return lunchEndWed;
    }

    public void setLunchEndWed(LocalTime lunchEndWed) {
        this.lunchEndWed = lunchEndWed;
    }

    public LocalTime getWorkStartThu() {
        return workStartThu;
    }

    public void setWorkStartThu(LocalTime workStartThu) {
        this.workStartThu = workStartThu;
    }

    public LocalTime getWorkEndThu() {
        return workEndThu;
    }

    public void setWorkEndThu(LocalTime workEndThu) {
        this.workEndThu = workEndThu;
    }

    public LocalTime getLunchStartThu() {
        return lunchStartThu;
    }

    public void setLunchStartThu(LocalTime lunchStartThu) {
        this.lunchStartThu = lunchStartThu;
    }

    public LocalTime getLunchEndThu() {
        return lunchEndThu;
    }

    public void setLunchEndThu(LocalTime lunchEndThu) {
        this.lunchEndThu = lunchEndThu;
    }

    public LocalTime getWorkStartFri() {
        return workStartFri;
    }

    public void setWorkStartFri(LocalTime workStartFri) {
        this.workStartFri = workStartFri;
    }

    public LocalTime getWorkEndFri() {
        return workEndFri;
    }

    public void setWorkEndFri(LocalTime workEndFri) {
        this.workEndFri = workEndFri;
    }

    public LocalTime getLunchStartFri() {
        return lunchStartFri;
    }

    public void setLunchStartFri(LocalTime lunchStartFri) {
        this.lunchStartFri = lunchStartFri;
    }

    public LocalTime getLunchEndFri() {
        return lunchEndFri;
    }

    public void setLunchEndFri(LocalTime lunchEndFri) {
        this.lunchEndFri = lunchEndFri;
    }

    public City getWorkCity() {
        return workCity;
    }

    public void setWorkCity(City workCity) {
        this.workCity = workCity;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerPreferences that = (WorkerPreferences) o;
        return workerExternalId.equals(that.workerExternalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workerExternalId);
    }
}
