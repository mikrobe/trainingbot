package com.jarics.trainbot.plan;

import java.util.List;

public class BikingPlanBuilder extends PlanBuilder {

    int[] pace = { 84, 103, 124, 147, 172 };
    double[] met = { 4.8, 5.9, 7.1, 8.4, 9.8 };
    double[] speed = { 10, 15, 20, 25, 30 };
    double sweetSpotRatio = 0.90;

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

    public List<Session> getVolumeSessions(int numberOfWeeks, double ftp, double distance) {
        return null;
        //        // =60*$B21/$C$29-$C$29*$C$27
        //        double[] plan = new double[volumePlan.length];
        //        for (int i = 0; i < volumePlan.length; i++) {
        //            plan[i] = 60 * volumePlan[i] / getSpeed(ftp) - getSpeed(ftp) * volumeRatio;
        //        }
        //        return plan;
    }

    /**
     * The formula is 60 * dist(km) / ftpSpeedSweetStop(km/hr)   (in our excel: =60*$C21/$C$30.
     * Sweetspot 90% of FTPSpeed.
     * @param intensityPlan
     * @param ftpWatts (watts)
     * @return
     */
    public List<Session> getIntervalsSessions(int numberOfWeeks, double ftp, double distance) {
        return null;
        //        double ftpSpeedSweetSpot = sweetSpotRatio * getSpeed(ftpWatts);
        //        double[] plan = new double[intensityPlan.length];
        //        for (int i = 0; i < intensityPlan.length; i++) {
        //            plan[i] = 60 * intensityPlan[i] / ftpSpeedSweetSpot;
        //        }
        //        return plan;
    }
}
