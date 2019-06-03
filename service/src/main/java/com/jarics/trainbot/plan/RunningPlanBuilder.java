package com.jarics.trainbot.plan;

import java.util.ArrayList;
import java.util.List;

/**
 * Return
 */
public class RunningPlanBuilder extends PlanBuilder {

    double ftpSweetspotLowRatio = 1.1;
    double ftpSweetspotHighRatio = 0.88;
    int[] pace = { 400, 327, 277, 240 };
    double[] met = { 8.8, 11.2, 12.9, 14.9 };
    double[] speed = { 9, 11, 13, 15 };

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

    /**
     * Returns the volume sessions
     * @param numberOfWeeks
     * @param ftp (last tested ftp)
     * @param distance (target race distance)
     * @return
     */
    public List<Session> getVolumeSessions(int numberOfWeeks, double ftp, double distance) {
        List<Session> sessions = new ArrayList<>();
        double targetVolumeFtp = ftp + ftp * ftpVolumeRatio; //$C$28+$C$28*$C$27 where C28 is FTP

        double[] volumeDistances = getVolumeDistances(distance, ftp, numberOfWeeks);
        for (int i = 0; i < numberOfWeeks; i++) {
            Session session = new Session();
            session.setTargetDistance(volumeDistances[i]);
            session.setTargetVolumeFtp(targetVolumeFtp);
            session.setTargetVolumeTime(volumeDistances[i] * targetVolumeFtp / 60); // =B21*($C$28+$C$28*$C$27)/60 where B21 is distance
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
            session.setTargetIntervalsHighTime(intervalsDistances[i] * targetHighFtp / 60);
            session.setTargetIntervalsLowTime(intervalsDistances[i] * targetLowFtp / 60);
            sessions.add(session);
        }
        return sessions;
    }

}
