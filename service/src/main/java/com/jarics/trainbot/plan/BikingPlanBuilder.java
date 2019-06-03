package com.jarics.trainbot.plan;

import java.util.ArrayList;
import java.util.List;

public class BikingPlanBuilder extends PlanBuilder {

    int[] pace = { 84, 103, 124, 147, 172 };
    double[] met = { 4.8, 5.9, 7.1, 8.4, 9.8 };
    double[] speed = { 10, 15, 20, 25, 30 };
    private double ftpSweetspotLowRatio = 0.97;
    private double ftpSweetspotHighRatio = 0.84;
    protected double ftpVolumeRatio = 0.3;

    public int[] getPace() {
        return pace;
    }

    public double[] getMet() {
        return met;
    }

    public double[] getSpeed() {
        return speed;
    }

    public double getMaximumVolumeRatio() {
        return maximumVolumeRatio;
    }

    public List<Session> getVolumeSessions_(int numberOfWeeks, double ftp, double distance) {
        return null;
        //        // =60*$B21/$C$29-$C$29*$C$27
        //        double[] plan = new double[volumePlan.length];
        //        for (int i = 0; i < volumePlan.length; i++) {
        //            plan[i] = 60 * volumePlan[i] / getSpeed(ftp) - getSpeed(ftp) * ftpVolumeRatio;
        //        }
        //        return plan;
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
        double targetVolumeFtp = ftp - ftp * ftpVolumeRatio;

        double[] volumeDistances = getVolumeDistances(distance, ftp, numberOfWeeks);
        for (int i = 0; i < numberOfWeeks; i++) {
            Session session = new Session();
            session.setTargetDistance(volumeDistances[i]);
            session.setTargetVolumeFtp(targetVolumeFtp);
            session.setTargetVolumeTime(60 * volumeDistances[i] / getSpeed(targetVolumeFtp));
            sessions.add(session);
        }
        return sessions;
    }

    /**
     * The formula is 60 * dist(km) / ftpSpeedSweetStop(km/hr)   (in our excel: =60*$C21/$C$30.
     * Sweetspot 90% of FTPSpeed.
     * @param intensityPlan
     * @param ftpWatts (watts)
     * @return
     */
    public List<Session> getIntervalsSessions_(int numberOfWeeks, double ftp, double distance) {
        return null;
        //        double ftpSpeedSweetSpot = sweetSpotRatio * getSpeed(ftpWatts);
        //        double[] plan = new double[intensityPlan.length];
        //        for (int i = 0; i < intensityPlan.length; i++) {
        //            plan[i] = 60 * intensityPlan[i] / ftpSpeedSweetSpot;
        //        }
        //        return plan;
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
            session.setTargetDistance(intervalsDistances[i]);
            session.setTargetHighFtp(targetHighFtp);
            session.setTargetLowFtp(targetLowFtp);
            session.setTargetIntervalsHighTime(60 * intervalsDistances[i] / targetHighFtp);
            session.setTargetIntervalsLowTime(60 * intervalsDistances[i] / targetLowFtp);
            sessions.add(session);
        }
        return sessions;
    }
}
