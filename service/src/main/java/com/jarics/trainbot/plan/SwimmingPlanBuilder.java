package com.jarics.trainbot.plan;

import java.util.ArrayList;
import java.util.List;

public class SwimmingPlanBuilder extends PlanBuilder {

    double ftpSweetspotLowRatio = 1.10;
    double ftpSweetspotHighRatio = 1.02;
    int[] pace = { 180, 144, 120, 103, 90 };
    double[] met = { 4.3, 6.8, 8.9, 11.5, 13.6 };
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

    public double getMaximumVolumeRatio() {
        return maximumVolumeRatio;
    }

    public List<Session> getVolumeSessions_(int numberOfWeeks, double ftp, double distance) {
        return null;
        //        // =($B2/100)*($C$28*$C$27+$C$28)/60
        //        double[] plan = new double[volumePlan.length];
        //        for (int i = 0; i < volumePlan.length; i++) {
        //            plan[i] = (volumePlan[i] / 100) * (ftp * ftpVolumeRatio + ftp) / 60;
        //        }
        //        return plan;
    }

    /**
     * The formula is dist(meter)/100m * ftpSweetStop(sec/100m) / 60  (in our excel: =($C2/100)*$C$29/60.
     * Sweetspot 102% to 110% of FTP. We change the ftp by sweetspot here.
     * @param numberOfWeeks
     * @param ftp
     * @param distance
     * @return
     */
    public List<Session> getIntervalsSessions_(int numberOfWeeks, double ftp, double distance) {
        return null;
        //        double ftpSweetStop = ftp - ftp *ftpSweetspotRatio;
        //        double[] plan = new double[intensityPlan.length];
        //        for (int i = 0; i < intensityPlan.length; i++) {
        //            plan[i] = (intensityPlan[i] / 100) * ftpSweetStop / 60;
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
        double targetVolumeFtp = ftp + ftp * ftpVolumeRatio;

        double[] volumeDistances = getVolumeDistances(distance, ftp, numberOfWeeks);
        for (int i = 0; i < numberOfWeeks; i++) {
            Session session = new Session();
            session.setTargetDistance(volumeDistances[i]);
            session.setTargetVolumeFtp(targetVolumeFtp);
            session.setTargetVolumeTime(volumeDistances[i] / 100 * targetVolumeFtp / 60); // (volumePlan[i] / 100) * (ftp * ftpVolumeRatio + ftp) / 60;
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
            session.setTargetDistance(intervalsDistances[i]);
            session.setTargetHighFtp(targetHighFtp);
            session.setTargetLowFtp(targetLowFtp);
            session.setTargetIntervalsHighTime(intervalsDistances[i] / 100 * targetHighFtp / 60);
            session.setTargetIntervalsLowTime(intervalsDistances[i] / 100 * targetLowFtp / 60);
            sessions.add(session);
        }
        return sessions;
    }
}
