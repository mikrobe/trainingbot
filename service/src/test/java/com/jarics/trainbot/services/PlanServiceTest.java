package com.jarics.trainbot.services;

import org.junit.Assert;
import org.junit.Test;

public class PlanServiceTest {

    double[] volumePlan = null;
    double[] intensityPlan = null;
    double[] volumeMinutesPlan = null;
    double[] intensityMinutesPlan = null;

    @Test
    public void swimmingPlanTest() {
        PlanService planService = new SwimmingPlanService();
        buildPlan(planService, 1500, 120, 20);
        Assert.assertEquals(2858.625, volumePlan[19], 1);
        Assert.assertEquals(1633.5, intensityPlan[19], 1);
        Assert.assertEquals(74.32424999999999, volumeMinutesPlan[19], 1);
        Assert.assertEquals(32.67, intensityMinutesPlan[19], 1);
    }

    @Test
    public void bikingPlanTest() {
        PlanService planService = new BikingPlanService();
        buildPlan(planService, 40, 147, 20);
        Assert.assertEquals(58.536, volumePlan[19], 1);
        Assert.assertEquals(43.36, intensityPlan[19], 1);
        Assert.assertEquals(132.9864, volumeMinutesPlan[19], 1);
        Assert.assertEquals(104.064, intensityMinutesPlan[19], 1);
    }

    @Test
    public void runningPlanTest() {
        PlanService planService = new RunningPlanService();
        buildPlan(planService, 10, 240, 20);
        Assert.assertEquals(15.5115, volumePlan[19], 1);
        Assert.assertEquals(11.49, intensityPlan[19], 1);
        Assert.assertEquals(80.65979999999999, volumeMinutesPlan[19], 1);
        Assert.assertEquals(45.96, intensityMinutesPlan[19], 1);

    }

    @Test
    public void runningPlanExtremeTest() {
        PlanService planService = new RunningPlanService();
        buildPlan(planService, 10, 80, 20);
        buildPlan(planService, 10, 800, 20);

    }

    private void buildPlan(PlanService planService, int distance, double ftp, int numberOfWeeks) {
        volumePlan = planService.getVolumePlan(distance, ftp, numberOfWeeks);
        intensityPlan = planService.getIntensityPlan(distance, ftp, numberOfWeeks);
        volumeMinutesPlan = planService.getVolumeTime(volumePlan, ftp);
        intensityMinutesPlan = planService.getIntensityTime(intensityPlan, ftp);

        for (int i = 0; i < volumePlan.length; i++) {
            System.out.print(i + 1 + ", ");
            System.out.print(volumePlan[i] + ", ");
            System.out.print(intensityPlan[i] + ", ");
            System.out.print(volumeMinutesPlan[i] + ", ");
            System.out.print(intensityMinutesPlan[i] + ", ");
            System.out.println();
        }
    }

}
