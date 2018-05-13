package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.Session;
import com.jarics.trainbot.entities.SimpleSession;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class TrainingBotService implements TrainingBotServiceIf {
    //deprecated
    double[] adjusters = {-0.41, 0.70, 0.10, 0.10};
    static double[] phases = {0.05, 0.05, -0.2, 0.1};
    //end deprecated

    static double[] shortIncreaseTable = {0.7, 0.1, 0.1, -0.41};
    static double[] longIncreaseTable = {0.05, 0.08, -0.10, 0.20};
    static double[] runDistanceStart = {21, 10, 5, 3}; //Marathon, Half, Olympic, Sprint distances
    static double[] runningSweetSpots = {1.02, 1.10}; //345
    static double[] bikingSweetSpots = {0.84, 0.97};


    public static void main(String args[]) {


    }

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

    private boolean validateOverTraining(int tw, int pNumberOfWeeks, List<Session> currentSessions) {
        //TODO linear regression if currentSessions ends up in overtraining or understraning
        //https://www.ibm.com/developerworks/library/os-weka1/
        return false;
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


    @Override
    public SimpleSession getSession(AthleteFTP pAthleteFTP, int pWeek) {
        double timeAtFtp = 15;
        double distance = runDistanceStart[pAthleteFTP.getTarget()];
        double boost = 1;

        timeAtFtp = timeAtFtp + (shortIncreaseTable[pWeek % 4] * timeAtFtp);
        distance = distance + (longIncreaseTable[pWeek % 4] * distance);
        SimpleSession wSimpleSession = new SimpleSession(pWeek, timeAtFtp, distance);
        wSimpleSession.setBikeLZone(bikingSweetSpots[0] * pAthleteFTP.getBikeFtp());
        wSimpleSession.setBikeHZone(bikingSweetSpots[1] * pAthleteFTP.getBikeFtp());
        wSimpleSession.setRunLZone(runningSweetSpots[0] * pAthleteFTP.getRunFtp());
        wSimpleSession.setRunHZone(runningSweetSpots[1] * pAthleteFTP.getRunFtp());
        return wSimpleSession;
    }

    @Override
    public List<SimpleSession> getSession(AthleteFTP pAthleteFTP) {
        int nbrWeeks = 20;
        double timeAtFtp = 15;
        double distance = runDistanceStart[pAthleteFTP.getTarget()];
        double boost = 1;

        List<SimpleSession> wSimpleSessions = new ArrayList<SimpleSession>();

        for (int i = 1; i < nbrWeeks + 1; i++) {

            int weekNbrInMonth = i % 4;
            timeAtFtp = timeAtFtp + (shortIncreaseTable[i % 4] * timeAtFtp);
            distance = distance + (longIncreaseTable[i % 4] * distance);
            SimpleSession wSimpleSession = new SimpleSession(i, timeAtFtp, distance);
            wSimpleSession.setBikeLZone(bikingSweetSpots[0] * pAthleteFTP.getBikeFtp());
            wSimpleSession.setBikeHZone(bikingSweetSpots[1] * pAthleteFTP.getBikeFtp());
            wSimpleSession.setRunLZone(runningSweetSpots[0] * pAthleteFTP.getRunFtp());
            wSimpleSession.setRunLZone(runningSweetSpots[1] * pAthleteFTP.getRunFtp());
            wSimpleSessions.add(wSimpleSession);
        }

        return wSimpleSessions;
    }


}
