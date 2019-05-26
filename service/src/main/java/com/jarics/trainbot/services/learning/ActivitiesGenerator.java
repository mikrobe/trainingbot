package com.jarics.trainbot.services.learning;

import com.jarics.trainbot.entities.AthleteActivity;
import com.jarics.trainbot.entities.BotActivityType;
import com.jarics.trainbot.entities.SimpleSession;

import java.util.ArrayList;
import java.util.List;

public class ActivitiesGenerator {

    public List<AthleteActivity> generateActivities(List<SimpleSession> wSimpleSessions) {
        List<AthleteActivity> wAthleteActivities = new ArrayList<>();
        for (SimpleSession wSimpleSession : wSimpleSessions) {
            wAthleteActivities.add(newSwimDistanceActivity(wSimpleSession));
            wAthleteActivities.add(newSwimIntensityActivity(wSimpleSession));
            wAthleteActivities.add(newBikeActivity(wSimpleSession));
            wAthleteActivities.add(newRunActivity(wSimpleSession));
        }
        return wAthleteActivities;
    }

    private AthleteActivity newBikeActivity(SimpleSession wSimpleSession) {
        AthleteActivity wActivity;
        wActivity = new AthleteActivity();
        wActivity.setWeekNbr(wSimpleSession.getWeek());
        wActivity.setType(BotActivityType.BIKE);
        wActivity.setDistance((float) wSimpleSession.getBikeDistance());
        wActivity.setMovingTime((int) Math.round(wSimpleSession.getBikeIntensityTime()) * 60);
        wActivity.setElapsedTime((int) Math.round(wSimpleSession.getBikeIntensityTime()) * 60);
        wActivity.setWeigthedAvgWatts((wSimpleSession.getBikeHZone() + wSimpleSession.getBikeHZone()) / 2);
        return wActivity;
    }

    private AthleteActivity newRunActivity(SimpleSession wSimpleSession) {
        AthleteActivity wActivity;
        wActivity = new AthleteActivity();
        wActivity.setWeekNbr(wSimpleSession.getWeek());
        wActivity.setType(BotActivityType.RUN);
        wActivity.setDistance((float) wSimpleSession.getRunDistance());
        wActivity.setMovingTime((int) Math.round(wSimpleSession.getRunIntensityTime()) * 60);
        wActivity.setElapsedTime((int) Math.round(wSimpleSession.getRunIntensityTime()) * 60);
        wActivity.setPace((wSimpleSession.getRunHZone() + wSimpleSession.getRunHZone()) / 2);
        return wActivity;
    }

    //TODO Zone 2 5 secs slower than T-Time
    private AthleteActivity newSwimDistanceActivity(SimpleSession pSimpleSession) {
        AthleteActivity wActivity = new AthleteActivity();
        wActivity.setWeekNbr(pSimpleSession.getWeek());
        wActivity.setType(BotActivityType.SWIM);
        wActivity.setDistance((float) pSimpleSession.getSwimDistance());
        wActivity.setMovingTime((int) Math.round(pSimpleSession.getSwimIntensityTime()) * 60);
        wActivity.setElapsedTime((int) Math.round(pSimpleSession.getSwimIntensityTime()) * 60);
        return wActivity;
    }

    private AthleteActivity newSwimIntensityActivity(SimpleSession pSimpleSession) {
        AthleteActivity wActivity = new AthleteActivity();
        wActivity.setType(BotActivityType.SWIM);
        double avgPace = (pSimpleSession.getSwimHZone() + pSimpleSession.getSwimLZone()) / 2;
        wActivity.setDistance((float) (pSimpleSession.getSwimIntensityTime() * 100 / avgPace));
        wActivity.setMovingTime((int) Math.round(pSimpleSession.getSwimIntensityTime()) * 60);
        wActivity.setElapsedTime((int) Math.round(pSimpleSession.getSwimIntensityTime()) * 60);
        wActivity.setPace(avgPace);
        return wActivity;
    }
}
