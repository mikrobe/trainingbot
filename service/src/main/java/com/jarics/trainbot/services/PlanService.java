package com.jarics.trainbot.services;

import org.springframework.stereotype.Component;

@Component
public abstract class PlanService {

    double[] weekIncrementRatio = { 0.1, 0.1, 0.1, -0.1 };

    public abstract double[] getVolumeTime(double[] volumePlan, double ftp);

    public abstract double[] getIntensityTime(double[] intensityPlan, double ftp);

    public abstract double getVolumeDistanceRatio();

    public abstract int[] getPace();

    public abstract double[] getSpeed();

    public abstract double[] getMet();

    public static void main(String[] args) {
        SwimmingPlanService swimmingPlanService = new SwimmingPlanService();
        double[] volumePlan = swimmingPlanService.getVolumePlan(1500, 120, 20);
        double[] intensityPlan = swimmingPlanService.getIntensityPlan(1500, 120, 20);
        double[] volumeMinutesPlan = swimmingPlanService.getVolumeTime(volumePlan, 120);
        double[] intensityMinutesPlan = swimmingPlanService.getIntensityTime(intensityPlan, 120);

        for (int i = 0; i < volumePlan.length; i++) {
            System.out.print(i + 1 + ", ");
            System.out.print(volumePlan[i] + ", ");
            System.out.print(intensityPlan[i] + ", ");
            System.out.print(volumeMinutesPlan[i] + ", ");
            System.out.print(intensityMinutesPlan[i] + ", ");
            System.out.println();
        }
    }

    public double[] getPlan(int numberOfWeeks, double distance) {
        double[] plan = new double[numberOfWeeks];
        plan[numberOfWeeks - 1] = distance;
        for (int i = numberOfWeeks - 2; i > -1; i--) {
            double volume = plan[i + 1] - weekIncrementRatio[(i + 1) % 4] * plan[i + 1];
            plan[i] = volume;
        }
        return plan;
    }

    public double getMet(double ftp) {
        int idx = getIdx(ftp);
        return getMet()[idx];
    }

    private int getIdx(double ftp) {
        double myNumber = ftp;
        double distance = Math.abs(getPace()[0] - myNumber);
        int idx = 0;
        for (int c = 1; c < getPace().length; c++) {
            double cdistance = Math.abs(getPace()[c] - myNumber);
            if (cdistance < distance) {
                idx = c;
                distance = cdistance;
            }
        }
        return idx;
    }

    public double getSpeed(double ftp) {
        int idx = getIdx(ftp);
        return getSpeed()[idx];
    }

    public double[] getVolumePlan(int raceDistance, double ftp, int numberOfWeeks) {
        double distanceVolume = getVolumeDistanceRatio() * raceDistance + raceDistance;
        double raceWeekDistanceVolume = distanceVolume + distanceVolume * getMet(ftp) / 100;
        return getPlan(numberOfWeeks, raceWeekDistanceVolume);
    }

    public double[] getIntensityPlan(int raceDistance, double ftp, int numberOfWeeks) {
        double distance = raceDistance + raceDistance * getMet(ftp) / 100;
        return getPlan(numberOfWeeks, distance);
    }
}
