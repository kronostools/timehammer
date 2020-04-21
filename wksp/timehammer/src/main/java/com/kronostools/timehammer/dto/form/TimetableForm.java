package com.kronostools.timehammer.dto.form;

import com.kronostools.timehammer.service.TimeMachineService;

import java.time.LocalTime;

public class TimetableForm {
    private String workTimeMon;
    private String lunchTimeMon;
    private String workTimeTue;
    private String lunchTimeTue;
    private String workTimeWed;
    private String lunchTimeWed;
    private String workTimeThu;
    private String lunchTimeThu;
    private String workTimeFri;
    private String lunchTimeFri;

    public String getWorkTimeMon() {
        return workTimeMon;
    }

    public void setWorkTimeMon(String workTimeMon) {
        this.workTimeMon = workTimeMon;
    }

    public String getLunchTimeMon() {
        return lunchTimeMon;
    }

    public void setLunchTimeMon(String lunchTimeMon) {
        this.lunchTimeMon = lunchTimeMon;
    }

    public String getWorkTimeTue() {
        return workTimeTue;
    }

    public void setWorkTimeTue(String workTimeTue) {
        this.workTimeTue = workTimeTue;
    }

    public String getLunchTimeTue() {
        return lunchTimeTue;
    }

    public void setLunchTimeTue(String lunchTimeTue) {
        this.lunchTimeTue = lunchTimeTue;
    }

    public String getWorkTimeWed() {
        return workTimeWed;
    }

    public void setWorkTimeWed(String workTimeWed) {
        this.workTimeWed = workTimeWed;
    }

    public String getLunchTimeWed() {
        return lunchTimeWed;
    }

    public void setLunchTimeWed(String lunchTimeWed) {
        this.lunchTimeWed = lunchTimeWed;
    }

    public String getWorkTimeThu() {
        return workTimeThu;
    }

    public void setWorkTimeThu(String workTimeThu) {
        this.workTimeThu = workTimeThu;
    }

    public String getLunchTimeThu() {
        return lunchTimeThu;
    }

    public void setLunchTimeThu(String lunchTimeThu) {
        this.lunchTimeThu = lunchTimeThu;
    }

    public String getWorkTimeFri() {
        return workTimeFri;
    }

    public void setWorkTimeFri(String workTimeFri) {
        this.workTimeFri = workTimeFri;
    }

    public String getLunchTimeFri() {
        return lunchTimeFri;
    }

    public void setLunchTimeFri(String lunchTimeFri) {
        this.lunchTimeFri = lunchTimeFri;
    }

    public LocalTime getWorkStarMon() {
        return TimeMachineService.getStartFromTimeInterval(this.workTimeMon);
    }

    public LocalTime getWorkEndMon() {
        return TimeMachineService.getEndFromTimeInterval(this.workTimeMon);
    }

    public LocalTime getLunchStarMon() {
        return TimeMachineService.getStartFromTimeInterval(this.lunchTimeMon);
    }

    public LocalTime getLunchEndMon() {
        return TimeMachineService.getEndFromTimeInterval(this.lunchTimeMon);
    }

    public LocalTime getWorkStarTue() {
        return TimeMachineService.getStartFromTimeInterval(this.workTimeTue);
    }

    public LocalTime getWorkEndTue() {
        return TimeMachineService.getEndFromTimeInterval(this.workTimeTue);
    }

    public LocalTime getLunchStarTue() {
        return TimeMachineService.getStartFromTimeInterval(this.lunchTimeTue);
    }

    public LocalTime getLunchEndTue() {
        return TimeMachineService.getEndFromTimeInterval(this.lunchTimeTue);
    }

    public LocalTime getWorkStarWed() {
        return TimeMachineService.getStartFromTimeInterval(this.workTimeWed);
    }

    public LocalTime getWorkEndWed() {
        return TimeMachineService.getEndFromTimeInterval(this.workTimeWed);
    }

    public LocalTime getLunchStarWed() {
        return TimeMachineService.getStartFromTimeInterval(this.lunchTimeWed);
    }

    public LocalTime getLunchEndWed() {
        return TimeMachineService.getEndFromTimeInterval(this.lunchTimeWed);
    }

    public LocalTime getWorkStarThu() {
        return TimeMachineService.getStartFromTimeInterval(this.workTimeThu);
    }

    public LocalTime getWorkEndThu() {
        return TimeMachineService.getEndFromTimeInterval(this.workTimeThu);
    }

    public LocalTime getLunchStarThu() {
        return TimeMachineService.getStartFromTimeInterval(this.lunchTimeThu);
    }

    public LocalTime getLunchEndThu() {
        return TimeMachineService.getEndFromTimeInterval(this.lunchTimeThu);
    }

    public LocalTime getWorkStarFri() {
        return TimeMachineService.getStartFromTimeInterval(this.workTimeFri);
    }

    public LocalTime getWorkEndFri() {
        return TimeMachineService.getEndFromTimeInterval(this.workTimeFri);
    }

    public LocalTime getLunchStarFri() {
        return TimeMachineService.getStartFromTimeInterval(this.lunchTimeFri);
    }

    public LocalTime getLunchEndFri() {
        return TimeMachineService.getEndFromTimeInterval(this.lunchTimeFri);
    }

    @Override
    public String toString() {
        return "TimetableForm{" +
                "workTimeMon='" + workTimeMon + '\'' +
                ", lunchTimeMon='" + lunchTimeMon + '\'' +
                ", workTimeTue='" + workTimeTue + '\'' +
                ", lunchTimeTue='" + lunchTimeTue + '\'' +
                ", workTimeWed='" + workTimeWed + '\'' +
                ", lunchTimeWed='" + lunchTimeWed + '\'' +
                ", workTimeThu='" + workTimeThu + '\'' +
                ", lunchTimeThu='" + lunchTimeThu + '\'' +
                ", workTimeFri='" + workTimeFri + '\'' +
                ", lunchTimeFri='" + lunchTimeFri + '\'' +
                '}';
    }
}