package com.jarics.trainbot.plan;

import java.util.ArrayList;
import java.util.List;

/**
 * Return
 */
public class RunningPlanBuilder extends PlanBuilder {

    int[] pace = { 400, 327, 277, 240 };
    double[] met = { 8.8, 11.2, 12.9, 14.9 };
    double[] speed = { 9, 11, 13, 15 };

    protected double ftpSweetSpotLowRatio = 1.1;
    protected double ftpSweetSpotHighRatio = 0.88;
    protected double ftpVolumeLowRatio = 1.5;
    protected double ftpVolumeHighRatio = 1.3;

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
            session.setSportType(SportType.run);
            session.setTargetDistance(volumeDistances[i]);
            session.setTargetHighFtp(targetHighFtp);
            session.setTargetLowFtp(targetLowFtp);
            session.setTargetHighTime(volumeDistances[i] * targetHighFtp / 60);
            session.setTargetLowTime(volumeDistances[i] * targetLowFtp / 60);
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
            session.setSportType(SportType.run);
            session.setTargetDistance(intervalsDistances[i]);
            session.setTargetHighFtp(targetHighFtp);
            session.setTargetLowFtp(targetLowFtp);
            session.setTargetHighTime(intervalsDistances[i] * targetHighFtp / 60);
            session.setTargetLowTime(intervalsDistances[i] * targetLowFtp / 60);
            sessions.add(session);
        }
        return sessions;
    }

}
