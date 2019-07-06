package com.jarics.trainbot.services.sessions;

import com.jarics.trainbot.plan.Plan;
import com.jarics.trainbot.plan.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PlanManager {

    @Value("${training.plan.reducer.ratio}") private double trainingPlanAdjusterRatio;

    public void reduceLoad(Plan plan) {
        plan
          .getPlannedWeeks()
          .forEach((n) -> reduceSession(n.getSwimVolumeSession()));
        plan
          .getPlannedWeeks()
          .forEach((n) -> reduceSession(n.getBikeVolumeSession()));
        plan
          .getPlannedWeeks()
          .forEach((n) -> reduceSession(n.getRunVolumeSession()));
        plan
          .getPlannedWeeks()
          .forEach((n) -> reduceSession(n.getSwimIntevalSession()));
        plan
          .getPlannedWeeks()
          .forEach((n) -> reduceSession(n.getBikeIntevalSession()));
        plan
          .getPlannedWeeks()
          .forEach((n) -> reduceSession(n.getRunIntevalSession()));
    }

    private void reduceSession(Session n) {
        n.setTargetHighTime((n.getTargetHighTime() - trainingPlanAdjusterRatio * n.getTargetHighTime()));
        n.setTargetLowTime((n.getTargetLowTime() - trainingPlanAdjusterRatio * n.getTargetLowTime()));
        n.setTargetDistance((n.getTargetDistance() - trainingPlanAdjusterRatio * n.getTargetDistance()));
    }

    public void increaseLoad(Plan plan) {
        plan
          .getPlannedWeeks()
          .forEach((n) -> increaseSession(n.getSwimVolumeSession()));
        plan
          .getPlannedWeeks()
          .forEach((n) -> increaseSession(n.getBikeVolumeSession()));
        plan
          .getPlannedWeeks()
          .forEach((n) -> increaseSession(n.getRunVolumeSession()));
        plan
          .getPlannedWeeks()
          .forEach((n) -> increaseSession(n.getSwimIntevalSession()));
        plan
          .getPlannedWeeks()
          .forEach((n) -> increaseSession(n.getBikeIntevalSession()));
        plan
          .getPlannedWeeks()
          .forEach((n) -> increaseSession(n.getRunIntevalSession()));
    }

    private void increaseSession(Session n) {
        n.setTargetHighTime((n.getTargetHighTime() + trainingPlanAdjusterRatio * n.getTargetHighTime()));
        n.setTargetLowTime((n.getTargetLowTime() + trainingPlanAdjusterRatio * n.getTargetLowTime()));
        n.setTargetDistance((n.getTargetDistance() + trainingPlanAdjusterRatio * n.getTargetDistance()));
    }

}
