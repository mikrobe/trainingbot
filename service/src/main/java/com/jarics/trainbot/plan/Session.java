package com.jarics.trainbot.plan;

public class Session {
    double targetDistance;
    double targetLowTime;
    double targetHighTime;
    double lastFtp;
    double targetHighFtp;
    double targetLowFtp;
    private SportType sportType;

    public Session() {
    }

    public double getTargetDistance() {
        return targetDistance;
    }

    public void setTargetDistance(double targetDistance) {
        this.targetDistance = targetDistance;
    }

    public double getTargetLowTime() {
        return targetLowTime;
    }

    public void setTargetLowTime(double targetLowTime) {
        this.targetLowTime = targetLowTime;
    }

    public double getTargetHighTime() {
        return targetHighTime;
    }

    public void setTargetHighTime(double targetHighTime) {
        this.targetHighTime = targetHighTime;
    }

    public double getLastFtp() {
        return lastFtp;
    }

    public void setLastFtp(double lastFtp) {
        this.lastFtp = lastFtp;
    }

    public double getTargetHighFtp() {
        return targetHighFtp;
    }

    public void setTargetHighFtp(double targetHighFtp) {
        this.targetHighFtp = targetHighFtp;
    }

    public double getTargetLowFtp() {
        return targetLowFtp;
    }

    public void setTargetLowFtp(double targetLowFtp) {
        this.targetLowFtp = targetLowFtp;
    }

    public SportType getSportType() {
        return sportType;
    }

    public void setSportType(SportType sportType) {
        this.sportType = sportType;
    }
}
