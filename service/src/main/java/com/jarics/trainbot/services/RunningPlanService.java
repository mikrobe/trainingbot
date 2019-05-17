package com.jarics.trainbot.services;

public class RunningPlanService extends PlanService {

    double ftpDistanceRatio = 0.3;
    double volumeDistanceRatio = 0.35;
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

    public double getVolumeDistanceRatio() {
        return volumeDistanceRatio;
    }

    public double[] getVolumeTime(double[] volumePlan, double ftp) {
        // =B21*($C$28+$C$28*$C$27)/60
        double[] plan = new double[volumePlan.length];
        for (int i = 0; i < volumePlan.length; i++) {
            plan[i] = volumePlan[i] * (ftp * ftpDistanceRatio + ftp) / 60;
        }
        return plan;
    }

    public double[] getIntensityTime(double[] intensityPlan, double ftp) {
        // =C21*$C$28/60
        double[] plan = new double[intensityPlan.length];
        for (int i = 0; i < intensityPlan.length; i++) {
            plan[i] = intensityPlan[i] * ftp / 60;
        }
        return plan;
    }
}
