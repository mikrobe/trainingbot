package com.jarics.trainbot.entities;

public class TrainingInfo {
    private int currentWeek;
    private int numberOfWeeks;
    private int ftp;
    private int targetFtp;
    private String sport;

    public int getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(int currentWeek) {
        this.currentWeek = currentWeek;
    }

    public int getNumberOfWeeks() {
        return numberOfWeeks;
    }

    public void setNumberOfWeeks(int numberOfWeeks) {
        this.numberOfWeeks = numberOfWeeks;
    }

    public int getFtp() {
        return ftp;
    }

    public void setFtp(int ftp) {
        this.ftp = ftp;
    }

    public int getTargetFtp() {
        return targetFtp;
    }

    public void setTargetFtp(int targetFtp) {
        this.targetFtp = targetFtp;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }
}
