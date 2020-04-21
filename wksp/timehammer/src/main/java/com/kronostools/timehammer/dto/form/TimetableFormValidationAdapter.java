package com.kronostools.timehammer.dto.form;

public class TimetableFormValidationAdapter implements TimetableFormValidation {
    private final TimetableForm timetable;

    public TimetableFormValidationAdapter(final TimetableForm timetable) {
        this.timetable = timetable;
    }


    @Override
    public String getWorkTimeMon() {
        return timetable.getWorkTimeMon();
    }

    @Override
    public String getLunchTimeMon() {
        return timetable.getLunchTimeMon();
    }

    @Override
    public String getWorkTimeTue() {
        return timetable.getWorkTimeTue();
    }

    @Override
    public String getLunchTimeTue() {
        return timetable.getLunchTimeTue();
    }

    @Override
    public String getWorkTimeWed() {
        return timetable.getWorkTimeWed();
    }

    @Override
    public String getLunchTimeWed() {
        return timetable.getLunchTimeWed();
    }

    @Override
    public String getWorkTimeThu() {
        return timetable.getWorkTimeThu();
    }

    @Override
    public String getLunchTimeThu() {
        return timetable.getLunchTimeThu();
    }

    @Override
    public String getWorkTimeFri() {
        return timetable.getWorkTimeFri();
    }

    @Override
    public String getLunchTimeFri() {
        return timetable.getLunchTimeFri();
    }
}