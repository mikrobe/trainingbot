package com.jarics.trainbot.services.sessions;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.SimpleSession;
import com.jarics.trainbot.services.EventTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TrainingPlanService implements TrainingPlanServiceIf {

    @Value("#{'${base.increase.ratio}'.split(',')}")
    private double[] baseIncreaseRatio;

    @Value("${beginner.weighted.increase.ratio}")
    private double beginnerWeightedIncreaseRatio;

    @Value("${intermediate.weighted.increase.ratio}")
    private double intermediateWeightedIncreaseRatio;

    @Value("${expert.weighted.increase.ratio}")
    private double expertWeightedIncreaseRatio;

    @Value("${start.intensity.time}")
    private double startIntensityTime;

    @Value("#{'${start.swim.distances}'.split(',')}")
    private double[] startSwimDistances;

    @Value("#{'${start.bike.distances}'.split(',')}")
    private double[] startBikeDistances;

    @Value("#{'${start.run.distances}'.split(',')}")
    private double[] startRunDistances;

    @Value("${target.swim.distance.ratio}")
    private double targetSwimDistanceRatio;

    @Value("${target.bike.distance.ratio}")
    private double targetBikeDistanceRatio;

    @Value("${target.run.distance.ratio}")
    private double targetRunDistanceRatio;

    @Value("${swim.low.sweet.spot}")
    private double swimLowSweetSpot;

    @Value("${swim.high.sweet.spot}")
    private double swimHighSweetSpot;

    @Value("${bike.low.sweet.spot}")
    private double bikeLowSweetSpot;

    @Value("${bike.high.sweet.spot}")
    private double bikeHighSweetSpot;

    @Value("${run.low.sweet.spot}")
    private double runLowSweetSpot;

    @Value("${run.high.sweet.spot}")
    private double runHighSweetSpot;

    /**
     * The first week is not weighted for intensity workouts. All other weeks have to
     * be weighted using athletes ranking and are calculated in incremental order.
     * For distance workouts, we establish the target distance (race distance + percentage) at
     * the end of the plan and decrease distances towards week 0. To make sure the athletes ends
     * the training plan on a easy week, the last hard phase is nbrWeek - 1. Hence, nbrWeek - 1
     * must be equal to target distance.
     *
     * @param athleteFTP
     * @param nbrWeeks
     * @return
     */
    @Override
    public List<SimpleSession> getSessions(AthleteFTP athleteFTP, int nbrWeeks) {

        List<SimpleSession> simpleSessions = new ArrayList<>();

        double[] intensityTimes = getIntensityTimes(athleteFTP, nbrWeeks, startIntensityTime);
        double[] swimDistances = getDistances(athleteFTP, nbrWeeks, startSwimDistances, targetSwimDistanceRatio);
        double[] bikeDistances = getDistances(athleteFTP, nbrWeeks, startBikeDistances, targetBikeDistanceRatio);
        double[] runDistances = getDistances(athleteFTP, nbrWeeks, startRunDistances, targetRunDistanceRatio);

        for (int i = 1; i < nbrWeeks; i++) {

            SimpleSession simpleSession =
                    new SimpleSession(
                            athleteFTP, i,
                            intensityTimes[i],
                            swimDistances[i],
                            bikeDistances[i],
                            runDistances[i],
                            swimLowSweetSpot * athleteFTP.getSwimFtp(),
                            swimHighSweetSpot * athleteFTP.getSwimFtp(),
                            bikeLowSweetSpot * athleteFTP.getBikeFtp(),
                            bikeHighSweetSpot * athleteFTP.getBikeFtp(),
                            runLowSweetSpot * athleteFTP.getRunFtp(),
                            runHighSweetSpot * athleteFTP.getRunFtp()
                    );
            simpleSessions.add(simpleSession);
        }
        return simpleSessions;
    }


    private double[] getDistances(AthleteFTP athleteFTP, int nbrWeeks, double[] startDistances, double targetDistanceRatio) {
        double[] distances = new double[nbrWeeks];
        double startDistance = getStartDistance(athleteFTP.getEventType(), startDistances);
        double athleteWeightedRatio = getAthleteWeightedRatio(athleteFTP, nbrWeeks % 4);
        distances[nbrWeeks - 2] = startDistance + (startDistance * targetDistanceRatio); //fixed week
        //=LOOKUP(mod($A21,4),weeksIncreases,beginner)*C20+C20
        distances[nbrWeeks - 1] = (athleteWeightedRatio * distances[nbrWeeks - 2]) + distances[nbrWeeks - 2]; // last week
        for (int i = nbrWeeks - 3; i > 0; i--) {
            //=C20-LOOKUP(mod($A19,4),weeksIncreases,beginner)*C20
            distances[i] = distances[i + 1] - (athleteWeightedRatio * distances[i + 1]);
        }
        return distances;
    }

    private double getStartDistance(EventTypes eventType, double[] startDistances) {
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
        intensityTimes[0] = startIntensityTime;
        for (int i = 1; i < nbrWeeks; i++) {
            int phase = i % 4;
            double athleteWeightedRatio = getAthleteWeightedRatio(athleteFTP, phase);
            //=LOOKUP(mod($A3,4),weeksIncreases,beginner)*B2+B2
            intensityTimes[i] = (athleteWeightedRatio * intensityTimes[i - 1]) + intensityTimes[i - 1];
        }
        return intensityTimes;
    }

    //replace with lambda function
    private double getAthleteWeightedRatio(AthleteFTP athleteFTP, int phase) {
        switch (athleteFTP.getAthletesRanking()) {
            case beginner: {
                return baseIncreaseRatio[phase] - beginnerWeightedIncreaseRatio;
            }
            case intermediate: {
                return baseIncreaseRatio[phase] - intermediateWeightedIncreaseRatio;
            }
            case expert: {
                return baseIncreaseRatio[phase] - expertWeightedIncreaseRatio;
            }
        }
        return 0;
    }

    double getIncrease(int pWeekPhase) {
        return baseIncreaseRatio[pWeekPhase];
    }
}
