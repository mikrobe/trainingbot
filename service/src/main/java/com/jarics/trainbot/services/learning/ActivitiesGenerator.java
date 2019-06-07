package com.jarics.trainbot.services.learning;

import com.jarics.trainbot.entities.AthleteActivity;
import com.jarics.trainbot.entities.BotActivityType;
import com.jarics.trainbot.plan.Session;

import java.util.ArrayList;
import java.util.List;

public class ActivitiesGenerator {

    public List<AthleteActivity> generateActivities(int week, List<Session> wSimpleSessions) {
        List<AthleteActivity> wAthleteActivities = new ArrayList<>();
        for (Session wSimpleSession : wSimpleSessions) {

            switch (wSimpleSession.getSportType()) {
            case swim: {
                wAthleteActivities.add(newSwimActivity(week, wSimpleSession));
                break;
            }
            case bike: {
                wAthleteActivities.add(newBikeActivity(week, wSimpleSession));
                break;
            }
            case run: {
                wAthleteActivities.add(newRunActivity(week, wSimpleSession));
                break;
            }
            }
        }
        return wAthleteActivities;
    }

    private AthleteActivity newBikeActivity(int week, Session wSimpleSession) {
        AthleteActivity wActivity;
        wActivity = new AthleteActivity();
        wActivity.setWeekNbr(week);
        wActivity.setType(BotActivityType.BIKE);
        wActivity.setDistance((float) wSimpleSession.getTargetDistance());
        wActivity.setMovingTime((int) Math.round((wSimpleSession.getTargetLowTime() + wSimpleSession.getTargetHighTime()) / 2));
        wActivity.setElapsedTime(wActivity.getMovingTime());
        wActivity.setWeigthedAvgWatts((wSimpleSession.getTargetHighFtp() + wSimpleSession.getTargetLowFtp()) / 2);
        return wActivity;
    }

    private AthleteActivity newRunActivity(int week, Session wSimpleSession) {
        AthleteActivity wActivity;
        wActivity = new AthleteActivity();
        wActivity.setWeekNbr(week);
        wActivity.setType(BotActivityType.RUN);
        wActivity.setDistance((float) wSimpleSession.getTargetDistance());
        wActivity.setMovingTime((int) Math.round((wSimpleSession.getTargetLowTime() + wSimpleSession.getTargetHighTime()) / 2));
        wActivity.setElapsedTime(wActivity.getMovingTime());
        wActivity.setPace((wSimpleSession.getTargetHighFtp() + wSimpleSession.getTargetLowFtp()) / 2);
        return wActivity;
    }

    private AthleteActivity newSwimActivity(int week, Session pSimpleSession) {
        AthleteActivity wActivity = new AthleteActivity();
        wActivity.setWeekNbr(week);
        wActivity.setType(BotActivityType.SWIM);
        wActivity.setDistance((float) pSimpleSession.getTargetDistance());
        wActivity.setMovingTime((int) Math.round((pSimpleSession.getTargetLowTime() + pSimpleSession.getTargetHighTime()) / 2));
        wActivity.setElapsedTime(wActivity.getMovingTime());
        wActivity.setPace((pSimpleSession.getTargetHighFtp() + pSimpleSession.getTargetLowFtp()) / 2);
        return wActivity;
    }

}
