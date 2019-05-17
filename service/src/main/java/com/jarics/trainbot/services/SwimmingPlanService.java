package com.jarics.trainbot.services;

public class SwimmingPlanService extends PlanService {

    double ftpDistanceRatio = 0.3;
    double volumeDistanceRatio = 0.75;
    int[] pace = { 180, 144, 120, 103, 90 };
    double[] met = { 4.3, 6.8, 8.9, 11.5, 13.6 };

    public int[] getPace() {
        return pace;
    }

    public double[] getMet() {
        return met;
    }

    public double[] getSpeed() {
        return null;
    }

    public double getVolumeDistanceRatio() {
        return volumeDistanceRatio;
    }

    public double[] getVolumeTime(double[] volumePlan, double ftp) {
        // =($B2/100)*($C$28*$C$27+$C$28)/60
        double[] plan = new double[volumePlan.length];
        for (int i = 0; i < volumePlan.length; i++) {
            plan[i] = (volumePlan[i] / 100) * (ftp * ftpDistanceRatio + ftp) / 60;
        }
        return plan;
    }

    public double[] getIntensityTime(double[] intensityPlan, double ftp) {
        // =($C2/100)*$C$28/60
        double[] plan = new double[intensityPlan.length];
        for (int i = 0; i < intensityPlan.length; i++) {
            plan[i] = (intensityPlan[i] / 100) * ftp / 60;
        }
        return plan;
    }
}
