package com.jarics.trainbot.entities;

public class SimpleSession {
    double timeAtFtp;
    double runDistance;
    double bikeDistance;
    int week;
    double runLZone;
    double runHZone;
    double bikeLZone;
    double bikeHZone;

    public SimpleSession() {
    }

    public SimpleSession(int week, double timeAtFtp) {
        setWeek(week);
        setTimeAtFtp(timeAtFtp);
    }

    public double getTimeAtFtp() {
        return timeAtFtp;
    }

    public void setTimeAtFtp(double timeAtFtp) {
        this.timeAtFtp = timeAtFtp;
    }


    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public double getRunLZone() {
        return runLZone;
    }

    public void setRunLZone(double runLZone) {
        this.runLZone = runLZone;
    }

    public double getRunHZone() {
        return runHZone;
    }

    public void setRunHZone(double runHZone) {
        this.runHZone = runHZone;
    }

    public double getBikeLZone() {
        return bikeLZone;
    }

    public void setBikeLZone(double bikeLZone) {
        this.bikeLZone = bikeLZone;
    }

    public double getBikeHZone() {
        return bikeHZone;
    }

    public void setBikeHZone(double bikeHZone) {
        this.bikeHZone = bikeHZone;
    }

    public double getRunDistance() {
        return runDistance;
    }

    public void setRunDistance(double runDistance) {
        this.runDistance = runDistance;
    }

    public double getBikeDistance() {
        return bikeDistance;
    }

    public void setBikeDistance(double bikeDistance) {
        this.bikeDistance = bikeDistance;
    }
}
