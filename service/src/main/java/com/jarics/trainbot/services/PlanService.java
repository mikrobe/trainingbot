package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.plan.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class PlanService {

    private static final double[] startSwimDistances = { 750, 1500, 1900, 3800 };

    private static final double[] startBikeDistances = { 20, 40, 90, 180 };

    private static final double[] startRunDistances = { 5, 10, 21.1, 42.2 };

    public Plan getTriathlonPlan(AthleteFTP athlete) {

        Plan plan = new Plan();
        int numberOfWeeks = getNumberOfWeeks(athlete.getRaceDate());

        SwimmingPlanBuilder swimmingPlanBuilder = new SwimmingPlanBuilder();
        List<Session> swimIntevalsSessions = swimmingPlanBuilder.getIntervalsSessions(numberOfWeeks, athlete.getSwimFtp(), getStartDistance(athlete.getEventType(), SportType.swim));
        List<Session> swimVolumeSessions = swimmingPlanBuilder.getVolumeSessions(numberOfWeeks, athlete.getSwimFtp(), getStartDistance(athlete.getEventType(), SportType.swim));

        BikingPlanBuilder bikingPlanBuilder = new BikingPlanBuilder();
        List<Session> bikeIntevalsSessions = bikingPlanBuilder.getIntervalsSessions(numberOfWeeks, athlete.getBikeFtp(), getStartDistance(athlete.getEventType(), SportType.bike));
        List<Session> bikeVolumeSessions = bikingPlanBuilder.getVolumeSessions(numberOfWeeks, athlete.getBikeFtp(), getStartDistance(athlete.getEventType(), SportType.bike));

        RunningPlanBuilder runningPlanBuilder = new RunningPlanBuilder();
        List<Session> runIntevalsSessions = runningPlanBuilder.getIntervalsSessions(numberOfWeeks, athlete.getRunFtp(), getStartDistance(athlete.getEventType(), SportType.run));
        List<Session> runVolumeSessions = runningPlanBuilder.getVolumeSessions(numberOfWeeks, athlete.getRunFtp(), getStartDistance(athlete.getEventType(), SportType.run));

        for (int i = 0; i < numberOfWeeks; i++) {
            PlannedWeek plannedWeek = new PlannedWeek();
            plannedWeek.setWeek(i);
            plannedWeek.setSwimIntevalSession(swimIntevalsSessions.get(i));
            plannedWeek.setSwimVolumeSession(swimVolumeSessions.get(i));
            plannedWeek.setBikeIntevalSession(bikeIntevalsSessions.get(i));
            plannedWeek.setBikeVolumeSession(bikeVolumeSessions.get(i));
            plannedWeek.setRunIntevalSession(runIntevalsSessions.get(i));
            plannedWeek.setRunVolumeSession(runVolumeSessions.get(i));
            plan.addPlannedWeek(plannedWeek);
        }
        return plan;
    }

    private int getNumberOfWeeks(Date raceDate) {
        return 20;
    }

    private double getStartDistance(EventType eventType, SportType sportType) {
        switch (sportType) {
        case swim: {
            return startSwimDistances[eventType.ordinal()];
        }
        case bike: {
            return startBikeDistances[eventType.ordinal()];
        }
        case run: {
            return startRunDistances[eventType.ordinal()];
        }
        }
        return 0;
    }

    public Plan getTriathlonOverTrainingPlan(AthleteFTP athlete) {

        Plan plan = new Plan();
        int numberOfWeeks = getNumberOfWeeks(athlete.getRaceDate());

        SwimmingOverPlanBuilder swimmingPlanBuilder = new SwimmingOverPlanBuilder();
        List<Session> swimIntevalsSessions = swimmingPlanBuilder.getIntervalsSessions(numberOfWeeks, athlete.getSwimFtp(), getStartDistance(athlete.getEventType(), SportType.swim));
        List<Session> swimVolumeSessions = swimmingPlanBuilder.getVolumeSessions(numberOfWeeks, athlete.getSwimFtp(), getStartDistance(athlete.getEventType(), SportType.swim));

        BikingOverPlanBuilder bikingPlanBuilder = new BikingOverPlanBuilder();
        List<Session> bikeIntevalsSessions = bikingPlanBuilder.getIntervalsSessions(numberOfWeeks, athlete.getBikeFtp(), getStartDistance(athlete.getEventType(), SportType.bike));
        List<Session> bikeVolumeSessions = bikingPlanBuilder.getVolumeSessions(numberOfWeeks, athlete.getBikeFtp(), getStartDistance(athlete.getEventType(), SportType.bike));

        RunningOverPlanBuilder runningPlanBuilder = new RunningOverPlanBuilder();
        List<Session> runIntevalsSessions = runningPlanBuilder.getIntervalsSessions(numberOfWeeks, athlete.getRunFtp(), getStartDistance(athlete.getEventType(), SportType.run));
        List<Session> runVolumeSessions = runningPlanBuilder.getVolumeSessions(numberOfWeeks, athlete.getRunFtp(), getStartDistance(athlete.getEventType(), SportType.run));

        for (int i = 0; i < numberOfWeeks; i++) {
            PlannedWeek plannedWeek = new PlannedWeek();
            plannedWeek.setWeek(i);
            plannedWeek.setSwimIntevalSession(swimIntevalsSessions.get(i));
            plannedWeek.setSwimVolumeSession(swimVolumeSessions.get(i));
            plannedWeek.setBikeIntevalSession(bikeIntevalsSessions.get(i));
            plannedWeek.setBikeVolumeSession(bikeVolumeSessions.get(i));
            plannedWeek.setRunIntevalSession(runIntevalsSessions.get(i));
            plannedWeek.setRunVolumeSession(runVolumeSessions.get(i));
            plan.addPlannedWeek(plannedWeek);
        }
        return plan;
    }

    public Plan getTriathlonUnderTrainingPlan(AthleteFTP athlete) {

        Plan plan = new Plan();
        int numberOfWeeks = getNumberOfWeeks(athlete.getRaceDate());

        SwimUnderPlanBuilder swimmingPlanBuilder = new SwimUnderPlanBuilder();
        List<Session> swimIntevalsSessions = swimmingPlanBuilder.getIntervalsSessions(numberOfWeeks, athlete.getSwimFtp(), getStartDistance(athlete.getEventType(), SportType.swim));
        List<Session> swimVolumeSessions = swimmingPlanBuilder.getVolumeSessions(numberOfWeeks, athlete.getSwimFtp(), getStartDistance(athlete.getEventType(), SportType.swim));

        BikingUnderPlanBuilder bikingPlanBuilder = new BikingUnderPlanBuilder();
        List<Session> bikeIntevalsSessions = bikingPlanBuilder.getIntervalsSessions(numberOfWeeks, athlete.getBikeFtp(), getStartDistance(athlete.getEventType(), SportType.bike));
        List<Session> bikeVolumeSessions = bikingPlanBuilder.getVolumeSessions(numberOfWeeks, athlete.getBikeFtp(), getStartDistance(athlete.getEventType(), SportType.bike));

        RunningUnderPlanBuilder runningPlanBuilder = new RunningUnderPlanBuilder();
        List<Session> runIntevalsSessions = runningPlanBuilder.getIntervalsSessions(numberOfWeeks, athlete.getRunFtp(), getStartDistance(athlete.getEventType(), SportType.run));
        List<Session> runVolumeSessions = runningPlanBuilder.getVolumeSessions(numberOfWeeks, athlete.getRunFtp(), getStartDistance(athlete.getEventType(), SportType.run));

        for (int i = 0; i < numberOfWeeks; i++) {
            PlannedWeek plannedWeek = new PlannedWeek();
            plannedWeek.setWeek(i);
            plannedWeek.setSwimIntevalSession(swimIntevalsSessions.get(i));
            plannedWeek.setSwimVolumeSession(swimVolumeSessions.get(i));
            plannedWeek.setBikeIntevalSession(bikeIntevalsSessions.get(i));
            plannedWeek.setBikeVolumeSession(bikeVolumeSessions.get(i));
            plannedWeek.setRunIntevalSession(runIntevalsSessions.get(i));
            plannedWeek.setRunVolumeSession(runVolumeSessions.get(i));
            plan.addPlannedWeek(plannedWeek);
        }
        return plan;
    }
}
