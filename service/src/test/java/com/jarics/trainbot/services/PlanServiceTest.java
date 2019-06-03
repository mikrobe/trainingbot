package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.plan.EventType;
import com.jarics.trainbot.plan.Plan;
import com.jarics.trainbot.plan.Session;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class PlanServiceTest {


    @Test
    public void runningPlanTest() {
        PlanService wPlanService = new PlanService();
        Plan plan = wPlanService.getTriathlonPlan(generateAthlete());

        Session volumeSession19 = plan
          .getPlannedWeeks()
          .get(19)
          .getRunVolumeSession();
        Session intervalSession19 = plan
          .getPlannedWeeks()
          .get(19)
          .getRunIntevalSession();

        Assert.assertEquals(15, volumeSession19.getTargetDistance(), 1);
        Assert.assertEquals(11, intervalSession19.getTargetDistance(), 1);
        Assert.assertEquals(80, volumeSession19.getTargetVolumeTime(), 1);
        Assert.assertEquals(50, intervalSession19.getTargetIntervalsLowTime(), 1);
        Assert.assertEquals(40, intervalSession19.getTargetIntervalsHighTime(), 1);
    }

    @Test
    public void swimmingPlanTest() {
        PlanService wPlanService = new PlanService();
        Plan plan = wPlanService.getTriathlonPlan(generateAthlete());

        Session volumeSession19 = plan
          .getPlannedWeeks()
          .get(19)
          .getSwimVolumeSession();
        Session intervalSession19 = plan
          .getPlannedWeeks()
          .get(19)
          .getSwimIntevalSession();

        Assert.assertEquals(2450, volumeSession19.getTargetDistance(), 1);
        Assert.assertEquals(1633, intervalSession19.getTargetDistance(), 1);
        Assert.assertEquals(234, volumeSession19.getTargetVolumeTime(), 1);
    }

    @Test
    public void bikingPlanTest() {
        PlanService wPlanService = new PlanService();
        Plan plan = wPlanService.getTriathlonPlan(generateAthlete());

        Session volumeSession19 = plan
          .getPlannedWeeks()
          .get(19)
          .getBikeVolumeSession();
        Session intervalSession19 = plan
          .getPlannedWeeks()
          .get(19)
          .getBikeIntevalSession();

        Assert.assertEquals(59, volumeSession19.getTargetDistance(), 1);
        Assert.assertEquals(43, intervalSession19.getTargetDistance(), 1);
        Assert.assertEquals(234, volumeSession19.getTargetVolumeTime(), 1);
    }


    @Test
    public void runningPlanExtremeTest() {
        //        PlanBuilder planService = new RunningPlanBuilder();
        //        buildPlan(planService, 10, 80, 20);
        //        buildPlan(planService, 10, 800, 20);

    }

    //    private void buildPlan(PlanBuilder planService, double distance, double ftp, int numberOfWeeks) {
    //        volumePlan = planService.getPlannedDistances(planService.getTargetVolume(distance), ftp, numberOfWeeks);
    //        intensityPlan = planService.getPlannedDistances(distance, ftp, numberOfWeeks);
    //        volumeMinutesPlan = planService.getVolumeTime(volumePlan, ftp);
    //        intensityMinutesPlan = planService.getIntensityTime(intensityPlan, ftp);
    //
    //        for (int i = 0; i < volumePlan.length; i++) {
    //            System.out.print(i + 1 + ", ");
    //            System.out.print(volumePlan[i] + ", ");
    //            System.out.print(intensityPlan[i] + ", ");
    //            System.out.print(volumeMinutesPlan[i] + ", ");
    //            System.out.print(intensityMinutesPlan[i] + ", ");
    //            System.out.println();
    //        }
    //    }

    private AthleteFTP generateAthlete() {
        AthleteFTP wAthleteFTP = new AthleteFTP();
        wAthleteFTP.setRunFtp(240.0); //5:45 --> 300+45 = 345
        wAthleteFTP.setBikeFtp(147);
        wAthleteFTP.setSwimFtp(120.0);
        wAthleteFTP.setEventType(EventType.olympic);
        wAthleteFTP.setUsername(UUID
          .randomUUID()
          .toString());
        return wAthleteFTP;
    }

}
