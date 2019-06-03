package com.jarics.trainbot.services.sessions;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.SimpleSession;
import com.jarics.trainbot.plan.EventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TrainingPlanService implements TrainingPlanServiceIf {

    @Value("#{'${base.intensity.increase.ratio}'.split(',')}") protected double[] baseIntensityIncreaseRatio;

    @Value("#{'${base.distance.increase.ratio}'.split(',')}") protected double[] baseDistanceIncreaseRatio;

    @Value("${beginner.weighted.increase.ratio}") private double beginnerWeightedIncreaseRatio;

    @Value("${intermediate.weighted.increase.ratio}") private double intermediateWeightedIncreaseRatio;

    @Value("${expert.weighted.increase.ratio}") private double expertWeightedIncreaseRatio;

    @Value("${start.intensity.time}") private double startIntensityTime;

    @Value("#{'${start.swim.distances}'.split(',')}") private double[] startSwimDistances;

    @Value("#{'${start.bike.distances}'.split(',')}") private double[] startBikeDistances;

    @Value("#{'${start.run.distances}'.split(',')}") private double[] startRunDistances;

    @Value("${target.swim.distance.ratio}") private double targetSwimDistanceRatio;

    @Value("${target.bike.distance.ratio}") private double targetBikeDistanceRatio;

    @Value("${target.run.distance.ratio}") private double targetRunDistanceRatio;

    @Value("${swim.low.sweet.spot}") private double swimLowSweetSpot;

    @Value("${swim.high.sweet.spot}") private double swimHighSweetSpot;

    @Value("${bike.low.sweet.spot}") private double bikeLowSweetSpot;

    @Value("${bike.high.sweet.spot}") private double bikeHighSweetSpot;

    @Value("${run.low.sweet.spot}") private double runLowSweetSpot;

    @Value("${run.high.sweet.spot}") private double runHighSweetSpot;

    @Override
    public List<SimpleSession> getSessions(AthleteFTP athleteFTP, int nbrWeeks) {

        List<SimpleSession> simpleSessions = new ArrayList<>();

        //        SwimmingPlanBuilder swimmingPlanService = new SwimmingPlanBuilder();
        //        BikingPlanBuilder bikingPlanService = new BikingPlanBuilder();
        //        RunningPlanBuilder runningPlanService = new RunningPlanBuilder();
        //
        //        double[] swimVolumePlan = swimmingPlanService.getPlannedDistances(swimmingPlanService.getTargetVolume(startSwimDistances[1]), athleteFTP.getSwimFtp(), 20);
        //        double[] swimIntensityPlan = swimmingPlanService.getPlannedDistances(startSwimDistances[1], athleteFTP.getSwimFtp(), 20);
        //        double[] swimVolumeMinutesPlan = swimmingPlanService.getVolumeTime(swimVolumePlan, athleteFTP.getSwimFtp());
        //        double[] swimIntensityMinutesPlan = swimmingPlanService.getIntensityTime(swimIntensityPlan, athleteFTP.getSwimFtp());
        //
        //        double[] bikeVolumePlan = bikingPlanService.getPlannedDistances(startBikeDistances[1], athleteFTP.getBikeFtp(), 20);
        //        double[] bikeIntensityPlan = bikingPlanService.getPlannedDistances(startBikeDistances[1], athleteFTP.getBikeFtp(), 20);
        //        double[] bikeVolumeMinutesPlan = bikingPlanService.getVolumeTime(bikeVolumePlan, athleteFTP.getBikeFtp());
        //        double[] bikeIntensityMinutesPlan = bikingPlanService.getIntensityTime(bikeIntensityPlan, athleteFTP.getBikeFtp());
        //
        //        double[] runVolumePlan = runningPlanService.getPlannedDistances(startRunDistances[1], athleteFTP.getRunFtp(), 20);
        //        double[] runIntensityPlan = runningPlanService.getPlannedDistances(startRunDistances[1], athleteFTP.getRunFtp(), 20);
        //        double[] runVolumeMinutesPlan = runningPlanService.getVolumeTime(runVolumePlan, athleteFTP.getRunFtp());
        //        double[] runIntensityMinutesPlan = runningPlanService.getIntensityTime(runIntensityPlan, athleteFTP.getRunFtp());
        //
        //        double[] bikeLowSweetSpots = getSweetSpots(athleteFTP, nbrWeeks, bikeLowSweetSpot * athleteFTP.getBikeFtp());
        //        double[] bikeHighSweetSpots = getSweetSpots(athleteFTP, nbrWeeks, bikeHighSweetSpot * athleteFTP.getBikeFtp());
        //
        //        for (int i = 0; i < nbrWeeks; i++) {
        //            SimpleSession simpleSession = new SimpleSession(athleteFTP, i, swimIntensityMinutesPlan[i], bikeIntensityMinutesPlan[i], runIntensityMinutesPlan[i], swimVolumePlan[i], bikeVolumePlan[i], runVolumePlan[i], swimLowSweetSpot * athleteFTP.getSwimFtp(),
        //              swimHighSweetSpot * athleteFTP.getSwimFtp(), bikeLowSweetSpots[i], bikeHighSweetSpots[i], runLowSweetSpot * athleteFTP.getRunFtp(), runHighSweetSpot * athleteFTP.getRunFtp());
        //            simpleSessions.add(simpleSession);
        //        }
        return simpleSessions;
    }

    protected double[] getSweetSpots(AthleteFTP athleteFTP, int nbrWeeks, double sweetSpot) {
        double[] sweetspots = new double[nbrWeeks];
        sweetspots[0] = sweetSpot;
        for (int i = 1; i < nbrWeeks; i++) {
            int phase = i % 4;
            double athleteWeightedRatio = getAthleteWeightedRatio(athleteFTP, phase, getBaseIntensityIncreaseRatio(phase));
            sweetspots[i] = athleteWeightedRatio * sweetSpot + sweetSpot;
        }
        return sweetspots;
    }

    private double[] getDistances(AthleteFTP athleteFTP, int nbrWeeks, double[] startDistances, double targetDistanceRatio) {
        double[] distances = new double[nbrWeeks];
        double startDistance = getStartDistance(athleteFTP.getEventType(), startDistances);
        distances[nbrWeeks - 2] = startDistance + (startDistance * targetDistanceRatio); //fixed week
        //=LOOKUP(mod($A21,4),weeksIncreases,beginner)*C20+C20
        int phase = (nbrWeeks - 1) % 4;
        double athleteWeightedRatio = getAthleteWeightedRatio(athleteFTP, phase, getBaseDistanceIncreaseRatio(phase));
        distances[nbrWeeks - 1] = distances[nbrWeeks - 2] - (athleteWeightedRatio * distances[nbrWeeks - 2]); // last week
        for (int i = nbrWeeks - 3; i > -1; i--) {
            //=C20-LOOKUP(mod($A19,4),weeksIncreases,beginner)*C20
            phase = i % 4;
            athleteWeightedRatio = getAthleteWeightedRatio(athleteFTP, phase, getBaseDistanceIncreaseRatio(phase));
            distances[i] = distances[i + 1] - (athleteWeightedRatio * distances[i + 1]);
        }
        return distances;
    }

    private double getStartDistance(EventType eventType, double[] startDistances) {
        switch (eventType) {
        case sprint: {
            return startDistances[0];
        }
        case olympic: {
            return startDistances[1];
        }
        case half: {
            return startDistances[2];
        }
        case ironman: {
            return startDistances[3];
        }
        }
        return 0;
    }

    private double[] getIntensityTimes(AthleteFTP athleteFTP, int nbrWeeks, double startIntensityTime) {
        double[] intensityTimes = new double[nbrWeeks];
        double athleteWeightedRatio = 0;
        intensityTimes[0] = startIntensityTime;
        for (int i = 1; i < nbrWeeks; i++) {
            int phase = i % 4;
            athleteWeightedRatio = getAthleteWeightedRatio(athleteFTP, phase, getBaseIntensityIncreaseRatio(phase));
            //=LOOKUP(mod($A3,4),weeksIncreases,beginner)*B2+B2
            intensityTimes[i] = (athleteWeightedRatio * intensityTimes[i - 1]) + intensityTimes[i - 1];
        }
        return intensityTimes;
    }

    private double getAthleteWeightedRatio(AthleteFTP athleteFTP, int phase, double phasedBaseIncreaseRatio) {
        double athleteWeightedRation = 0;
        switch (athleteFTP.getAthletesRanking()) {
        case beginner: {
            athleteWeightedRation = phasedBaseIncreaseRatio - beginnerWeightedIncreaseRatio;
            break;
        }
        case intermediate: {
            athleteWeightedRation = phasedBaseIncreaseRatio - intermediateWeightedIncreaseRatio;
            break;
        }
        case expert: {
            athleteWeightedRation = phasedBaseIncreaseRatio - expertWeightedIncreaseRatio;
            break;
        }
        }
        return (phase == 4 ? -1 * athleteWeightedRation : athleteWeightedRation);
    }

    protected double getBaseIntensityIncreaseRatio(int phase) {
        return baseIntensityIncreaseRatio[phase];
    }

    protected double getBaseDistanceIncreaseRatio(int phase) {
        return baseDistanceIncreaseRatio[phase];
    }

}
