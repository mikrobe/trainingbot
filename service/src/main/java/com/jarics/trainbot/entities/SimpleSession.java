package com.jarics.trainbot.entities;

public class SimpleSession {
    double timeAtFtp;
    double distance;
    int week;

    public SimpleSession() {
    }

    public SimpleSession(int week, double timeAtFtp, double distance) {
        setDistance(distance);
        setWeek(week);
        setTimeAtFtp(timeAtFtp);
    }

    public double getTimeAtFtp() {
        return timeAtFtp;
    }

    public void setTimeAtFtp(double timeAtFtp) {
        this.timeAtFtp = timeAtFtp;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }
}
