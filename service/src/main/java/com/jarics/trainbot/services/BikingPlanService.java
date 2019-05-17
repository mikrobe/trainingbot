package com.jarics.trainbot.services;

public class BikingPlanService extends PlanService {

    int[] pace = { 84, 103, 124, 147, 172 };
    double[] met = { 4.8, 5.9, 7.1, 8.4, 9.8 };
    double[] speed = { 10, 15, 20, 25, 30 };
    double ftpDistanceRatio = 0.3;
    double volumeDistanceRatio = 0.35;

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
        // =60*$B21/$C$29-$C$29*$C$27
        double[] plan = new double[volumePlan.length];
        for (int i = 0; i < volumePlan.length; i++) {
            plan[i] = 60 * volumePlan[i] / getSpeed(ftp) - getSpeed(ftp) * ftpDistanceRatio;
        }
        return plan;
    }

    public double[] getIntensityTime(double[] intensityPlan, double ftp) {
        // =60*$C21/$C$29
        double[] plan = new double[intensityPlan.length];
        for (int i = 0; i < intensityPlan.length; i++) {
            plan[i] = 60 * intensityPlan[i] / getSpeed(ftp);
        }
        return plan;
    }
}
