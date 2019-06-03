package com.jarics.trainbot.plan;

import java.util.List;

public class SwimmingPlanBuilder extends PlanBuilder {

    double ftpSweetspotRatio = 0.06;
    int[] pace = { 180, 144, 120, 103, 90 };
    double[] met = { 4.3, 6.8, 8.9, 11.5, 13.6 };

    protected double maximumVolumeRatio = 0.35;

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

    public List<Session> getVolumeSessions(int numberOfWeeks, double ftp, double distance) {
        return null;
        //        // =($B2/100)*($C$28*$C$27+$C$28)/60
        //        double[] plan = new double[volumePlan.length];
        //        for (int i = 0; i < volumePlan.length; i++) {
        //            plan[i] = (volumePlan[i] / 100) * (ftp * volumeRatio + ftp) / 60;
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
    public List<Session> getIntervalsSessions(int numberOfWeeks, double ftp, double distance) {
        return null;
        //        double ftpSweetStop = ftp - ftp *ftpSweetspotRatio;
        //        double[] plan = new double[intensityPlan.length];
        //        for (int i = 0; i < intensityPlan.length; i++) {
        //            plan[i] = (intensityPlan[i] / 100) * ftpSweetStop / 60;
        //        }
        //        return plan;
    }
}
