package com.jarics.trainbot.plan;

import java.util.ArrayList;
import java.util.List;

public class SwimmingPlanBuilder extends PlanBuilder {

    /** Start statistical values - don't change **/
    static final int[] pace = { 180, 144, 120, 103, 90 };
    static final double[] met = { 4.3, 6.8, 8.9, 11.5, 13.6 };
    static final double[] speed = { 180, 144, 120, 103, 90 };
    /** End statistical values - don't change **/

    protected double ftpSweetspotLowRatio = 1.10;
    protected double ftpSweetspotHighRatio = 1.02;
    protected double ftpVolumeLowRatio = 0.56;
    protected double ftpVolumeHighRatio = 0.90;

    /** overrides PlanBuilder **/
    protected double maximumVolumeRatio = 0.5;

    public int[] getPace() {
        return pace;
    }

    public double[] getMet() {
        return met;
    }

    public double[] getSpeed() {
        return null;
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
            session.setSportType(SportType.swim);
            session.setTargetDistance(volumeDistances[i]);
            session.setTargetHighFtp(targetHighFtp);
            session.setTargetLowFtp(targetLowFtp);
            session.setTargetHighTime(volumeDistances[i] / 100 * targetHighFtp / 60);
            session.setTargetLowTime(volumeDistances[i] / 100 * targetLowFtp / 60);
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
        double targetHighFtp = ftp * ftpSweetspotHighRatio;
        double targetLowFtp = ftp * ftpSweetspotLowRatio;
        double[] intervalsDistances = getIntervalsDistances(distance, ftp, numberOfWeeks);
        for (int i = 0; i < numberOfWeeks; i++) {
            Session session = new Session();
            session.setSportType(SportType.swim);
            session.setTargetDistance(intervalsDistances[i]);
            session.setTargetHighFtp(targetHighFtp);
            session.setTargetLowFtp(targetLowFtp);
            session.setTargetHighFtp(targetHighFtp);
            session.setTargetLowFtp(targetLowFtp);
            session.setTargetHighTime(intervalsDistances[i] / 100 * targetHighFtp / 60);
            session.setTargetLowTime(intervalsDistances[i] / 100 * targetLowFtp / 60);
            sessions.add(session);
        }
        return sessions;
    }
}
