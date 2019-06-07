package com.jarics.trainbot.plan;

import java.util.ArrayList;
import java.util.List;

public class BikingPlanBuilder extends PlanBuilder {

    int[] pace = { 84, 103, 124, 147, 172 };
    double[] met = { 4.8, 5.9, 7.1, 8.4, 9.8 };
    double[] speed = { 10, 15, 20, 25, 30 };
    protected double ftpSweetSpotLowRatio = 0.97;
    protected double ftpSweetSpotHighRatio = 0.84;
    protected double ftpVolumeLowRatio = 0.56;
    protected double ftpVolumeHighRatio = 0.90;


    public int[] getPace() {
        return pace;
    }

    public double[] getMet() {
        return met;
    }

    public double[] getSpeed() {
        return speed;
    }

    protected double getMaximumVolumeRatio() {
        return maximumVolumeRatio;
    }

    /**
     * Returns the volume sessions
     * @param numberOfWeeks
     * @param ftp (last tested ftp)
     * @param distance (target race distance)
     * @return
     */
    public List<Session> getVolumeSessions(int numberOfWeeks, double ftp, double distance) {
        List<Session> sessions = new ArrayList<>();
        double targetHighFtp = ftp * ftpVolumeHighRatio;
        double targetLowFtp = ftp * ftpVolumeLowRatio;
        double[] volumeDistances = getVolumeDistances(distance, ftp, numberOfWeeks);
        for (int i = 0; i < numberOfWeeks; i++) {
            Session session = new Session();
            session.setSportType(SportType.bike);
            session.setTargetDistance(volumeDistances[i]);
            session.setTargetLowFtp(targetLowFtp);
            session.setTargetHighFtp(targetHighFtp);
            session.setTargetLowTime(60 * volumeDistances[i] / getSpeed(targetLowFtp));
            session.setTargetHighTime(60 * volumeDistances[i] / getSpeed(targetHighFtp));
            sessions.add(session);
        }
        return sessions;
    }

    /**
     * Returns all interval sessions.
     * The formula is dist(km) * ftp(sec/km) / 60  (in our excel: =C21*$C$28/60).
     * Sweetspot 102% to 110% of FTP. We change the ftp by sweetspot here.
     * @return
     */
    public List<Session> getIntervalsSessions(int numberOfWeeks, double ftp, double distance) {
        List<Session> sessions = new ArrayList<>();
        double targetHighFtp = ftp * ftpSweetSpotHighRatio;
        double targetLowFtp = ftp * ftpSweetSpotLowRatio;
        double[] intervalsDistances = getIntervalsDistances(distance, ftp, numberOfWeeks);
        for (int i = 0; i < numberOfWeeks; i++) {
            Session session = new Session();
            session.setSportType(SportType.bike);
            session.setTargetDistance(intervalsDistances[i]);
            session.setTargetHighFtp(targetHighFtp);
            session.setTargetLowFtp(targetLowFtp);
            session.setTargetHighTime(60 * intervalsDistances[i] / targetHighFtp);
            session.setTargetLowTime(60 * intervalsDistances[i] / targetLowFtp);
            sessions.add(session);
        }
        return sessions;
    }
}
