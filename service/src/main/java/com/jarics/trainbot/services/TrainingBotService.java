package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.Session;
import com.jarics.trainbot.entities.SimpleSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class TrainingBotService implements TrainingBotServiceIf {

    static double[] phases = {0.05, 0.05, -0.2, 0.1};
    static double[] shortIncreaseTable = {0.7, 0.1, 0.1, -0.41};
    static double[] longIncreaseTable = {0.05, 0.08, -0.10, 0.20};
    static double[] runDistanceStart = {21, 10, 5, 3}; //Ironman, Half, Olympic, Sprint distances
    static double[] bikeDistanceStart = {90, 45, 20, 10}; //Ironman, Half, Olympic, Sprint distances
    //end deprecated
    static double[] swimDistanceStart = {1900, 950, 750, 375}; //Ironman, Half, Olympic, Sprint distances
    static double[] runningSweetSpots = {1.02, 1.10};
    static double[] bikingSweetSpots = {0.84, 0.97};
    static double[] swimSweetSpots = {1.02, 1.10};
    @Autowired
    StravaService stravaService;
    @Autowired
    MLService mlService;
    @Autowired
    AthleteRepositoryService athleteRepositoryService;
    //deprecated
    double[] adjusters = {-0.41, 0.70, 0.10, 0.10};

    public static int getShort(int session, int lastsecs) {
        return lastsecs - 1;
    }

    public static long getLong(long lastsecs) {
        return lastsecs;
    }

    public static double getPhase(int nbrWeekInMonth, double boost) {
        // 0.1, 0,1, -0.41, 0.70
        return phases[nbrWeekInMonth] * boost;
    }

    private static WeekFocus getWeekFocus(int tw, int pNumberOfWeeks) {
        double[] breakFocus = {0.74, 0, 76};
        double[] repeatingFocus = {0.84, 0.86};
        double r = tw / pNumberOfWeeks;
        if (tw == 1)
            return WeekFocus.firstWeek;
        if (r >= breakFocus[0] && r <= breakFocus[1])
            return WeekFocus.breakWeek;
        if (r >= repeatingFocus[0] && r <= repeatingFocus[1])
            return WeekFocus.repeeting;
        return WeekFocus.regular;
    }

    private boolean validateOverTraining(int tw, int pNumberOfWeeks, List<Session> currentSessions) {
        //TODO linear regression if currentSessions ends up in overtraining or understraning
        //https://www.ibm.com/developerworks/library/os-weka1/
        return false;
    }

    @Override
    public SimpleSession getSession(AthleteFTP pAthleteFTP, int pWeek) {
        return getSession(pAthleteFTP).get(pWeek - 1);
    }

    @Override
    public List<SimpleSession> getSession(AthleteFTP pAthleteFTP) {
        int nbrWeeks = 20;
        double timeAtFtp = 15;
        AthleteFTP wAthleteFTP = null;

        //persistence
        wAthleteFTP = athleteRepositoryService.getAthleteFTP(pAthleteFTP);

        double runDistance = runDistanceStart[wAthleteFTP.getTarget()];
        double bikeDistance = bikeDistanceStart[wAthleteFTP.getTarget()];
        double swimDistance = swimDistanceStart[wAthleteFTP.getTarget()];
        double boost = 1;

        List<SimpleSession> wSimpleSessions = new ArrayList<SimpleSession>();

        for (int i = 1; i < nbrWeeks + 1; i++) {

            timeAtFtp = timeAtFtp + (shortIncreaseTable[i % 4] * timeAtFtp);
            runDistance = runDistance + (longIncreaseTable[i % 4] * runDistance);
            bikeDistance = bikeDistance + (longIncreaseTable[i % 4] * bikeDistance);
            swimDistance = swimDistance + (longIncreaseTable[i % 4] * swimDistance);
            SimpleSession wSimpleSession = new SimpleSession(i, timeAtFtp);
            wSimpleSession.setRunDistance(runDistance);
            wSimpleSession.setBikeDistance(bikeDistance);
            wSimpleSession.setSwimDistance(swimDistance);
            wSimpleSession.setBikeLZone(bikingSweetSpots[0] * wAthleteFTP.getBikeFtp());
            wSimpleSession.setBikeHZone(bikingSweetSpots[1] * wAthleteFTP.getBikeFtp());
            wSimpleSession.setRunLZone(runningSweetSpots[0] * wAthleteFTP.getRunFtp());
            wSimpleSession.setRunHZone(runningSweetSpots[1] * wAthleteFTP.getRunFtp());
            wSimpleSession.setSwimLZone(swimSweetSpots[0] * wAthleteFTP.getSwimFtp());
            wSimpleSession.setSwimHZone(swimSweetSpots[1] * wAthleteFTP.getSwimFtp());
            wSimpleSessions.add(wSimpleSession);
        }

        return wSimpleSessions;
    }


}
