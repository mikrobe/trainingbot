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
    public void run_volume_for_week_before_race_week_must_higher_by_10_percent() {
        PlanService wPlanService = new PlanService();
        Plan plan = wPlanService.getTriathlonPlan(generateAthlete());

        Session volumeSession18 = plan
          .getPlannedWeeks()
          .get(18)
          .getRunVolumeSession();

        Session volumeSession19 = plan
          .getPlannedWeeks()
          .get(19)
          .getRunVolumeSession();

        double differenceBetweenWeek18And19 = (volumeSession19.getTargetDistance() - volumeSession18.getTargetDistance()) / volumeSession18.getTargetDistance();//=(B21-B20)/B20

        Assert.assertEquals(-0.09, differenceBetweenWeek18And19, 1);
    }

    @Test
    public void run_last_week_volume_and_intensity_distances_and_times() {
        PlanService wPlanService = new PlanService();
        Plan plan = wPlanService.getTriathlonPlan(generateAthlete());

        Session volumeSession19 = plan
          .getPlannedWeeks()
          .get(19)
          .getRunVolumeSession();
        Session intensitySession19 = plan
          .getPlannedWeeks()
          .get(19)
          .getRunIntevalSession();

        Assert.assertEquals(15, volumeSession19.getTargetDistance(), 1);
        Assert.assertEquals(11, intensitySession19.getTargetDistance(), 1);
        Assert.assertEquals(93, volumeSession19.getTargetLowTime(), 1);
        Assert.assertEquals(81, volumeSession19.getTargetHighTime(), 1);
        Assert.assertEquals(51, intensitySession19.getTargetLowTime(), 1);
        Assert.assertEquals(45, intensitySession19.getTargetHighTime(), 1);
    }

    @Test
    public void swim_volume_for_week_before_race_week_must_higher_by_10_percent() {
        PlanService wPlanService = new PlanService();
        Plan plan = wPlanService.getTriathlonPlan(generateAthlete());

        Session volumeSession18 = plan
          .getPlannedWeeks()
          .get(18)
          .getSwimVolumeSession();

        Session volumeSession19 = plan
          .getPlannedWeeks()
          .get(19)
          .getSwimVolumeSession();

        double differenceBetweenWeek18And19 = (volumeSession19.getTargetDistance() - volumeSession18.getTargetDistance()) / volumeSession18.getTargetDistance();//=(B21-B20)/B20

        Assert.assertEquals(-0.09, differenceBetweenWeek18And19, 1);
    }


    @Test
    public void swim_last_week_volume_and_intensity_distances_and_times() {
        PlanService wPlanService = new PlanService();
        Plan plan = wPlanService.getTriathlonPlan(generateAthlete());

        Session volumeSession19 = plan
          .getPlannedWeeks()
          .get(19)
          .getSwimVolumeSession();
        Session intensitySession19 = plan
          .getPlannedWeeks()
          .get(19)
          .getSwimIntevalSession();

        Assert.assertEquals(2450, volumeSession19.getTargetDistance(), 1);
        Assert.assertEquals(1634, intensitySession19.getTargetDistance(), 1);
    }

    @Test
    public void bike_volume_for_week_before_race_week_must_higher_by_10_percent() {
        PlanService wPlanService = new PlanService();
        Plan plan = wPlanService.getTriathlonPlan(generateAthlete());

        Session volumeSession18 = plan
          .getPlannedWeeks()
          .get(18)
          .getBikeVolumeSession();

        Session volumeSession19 = plan
          .getPlannedWeeks()
          .get(19)
          .getBikeVolumeSession();

        double differenceBetweenWeek18And19 = (volumeSession19.getTargetDistance() - volumeSession18.getTargetDistance()) / volumeSession18.getTargetDistance();//=(B21-B20)/B20

        Assert.assertEquals(-0.09, differenceBetweenWeek18And19, 1);
    }

    @Test
    public void bike_last_week_volume_and_intensity_distances_and_times() {
        PlanService wPlanService = new PlanService();
        Plan plan = wPlanService.getTriathlonPlan(generateAthlete());

        Session volumeSession19 = plan
          .getPlannedWeeks()
          .get(19)
          .getBikeVolumeSession();
        Session intensitySession19 = plan
          .getPlannedWeeks()
          .get(19)
          .getBikeIntevalSession();

        Assert.assertEquals(50, volumeSession19.getTargetDistance(), 1);
        Assert.assertEquals(43, intensitySession19.getTargetDistance(), 1);
    }


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
