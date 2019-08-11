package com.jarics.trainbot.plan;

public class PlannedWeek {
    int week;
    Session swimIntevalSession;
    Session swimVolumeSession;
    Session bikeIntevalSession;
    Session bikeVolumeSession;
    Session runIntevalSession;
    Session runVolumeSession;

    public PlannedWeek() {
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public Session getSwimIntevalSession() {
        return swimIntevalSession;
    }

    public void setSwimIntevalSession(Session swimIntevalSession) {
        this.swimIntevalSession = swimIntevalSession;
    }

    public Session getSwimVolumeSession() {
        return swimVolumeSession;
    }

    public void setSwimVolumeSession(Session swimVolumeSession) {
        this.swimVolumeSession = swimVolumeSession;
    }

    public Session getBikeIntevalSession() {
        return bikeIntevalSession;
    }

    public void setBikeIntevalSession(Session bikeIntevalSession) {
        this.bikeIntevalSession = bikeIntevalSession;
    }

    public Session getBikeVolumeSession() {
        return bikeVolumeSession;
    }

    public void setBikeVolumeSession(Session bikeVolumeSession) {
        this.bikeVolumeSession = bikeVolumeSession;
    }

    public Session getRunIntevalSession() {
        return runIntevalSession;
    }

    public void setRunIntevalSession(Session runIntevalSession) {
        this.runIntevalSession = runIntevalSession;
    }

    public Session getRunVolumeSession() {
        return runVolumeSession;
    }

    public void setRunVolumeSession(Session runVolumeSession) {
        this.runVolumeSession = runVolumeSession;
    }
}
