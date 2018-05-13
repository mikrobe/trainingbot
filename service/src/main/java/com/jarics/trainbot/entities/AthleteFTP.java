package com.jarics.trainbot.entities;

public class AthleteFTP {
    double bikeFtp;
    double runFtp;
    int target; // 0,1,2,3 for ironman, 70.3, olympic, sprint

    public AthleteFTP() {
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public double getBikeFtp() {
        return bikeFtp;
    }

    public void setBikeFtp(double bikeFtp) {
        this.bikeFtp = bikeFtp;
    }

    public double getRunFtp() {
        return runFtp;
    }

    public void setRunFtp(double runFtp) {
        this.runFtp = runFtp;
    }

}
