package com.jarics.trainbot.entities;

public class AthleteFTP {
    double ftp;
    int target; // 0,1,2,3 for ironman, 70.3, olympic, sprint

    public AthleteFTP() {
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public double getFtp() {
        return ftp;
    }

    public void setFtp(double ftp) {
        this.ftp = ftp;
    }
}
