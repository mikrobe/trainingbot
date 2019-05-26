package com.jarics.trainbot.services.sessions;

import com.jarics.trainbot.entities.SimpleSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SessionManager {

    @Value("${training.plan.reducer.ratio}") private double trainingPlanAdjusterRatio;

    public void reduceLoad(List<SimpleSession> sessions) {
        sessions.forEach((n) -> reduceSession(n));
    }

    private void reduceSession(SimpleSession n) {
        n.setSwimIntensityTime((n.getSwimIntensityTime() - trainingPlanAdjusterRatio * n.getSwimIntensityTime()));
        n.setBikeIntensityTime((n.getBikeIntensityTime() - trainingPlanAdjusterRatio * n.getBikeIntensityTime()));
        n.setRunIntensityTime((n.getRunIntensityTime() - trainingPlanAdjusterRatio * n.getRunIntensityTime()));
        n.setBikeDistance(n.getBikeDistance() - trainingPlanAdjusterRatio * n.getBikeDistance());
        n.setSwimDistance(n.getSwimDistance() - trainingPlanAdjusterRatio * n.getSwimDistance());
        n.setRunDistance(n.getRunDistance() - trainingPlanAdjusterRatio * n.getRunDistance());
    }

    public void increaseLoad(List<SimpleSession> sessions) {
        sessions.forEach((n) -> increaseSession(n));
    }

    private void increaseSession(SimpleSession n) {
        n.setSwimIntensityTime((n.getSwimIntensityTime() + trainingPlanAdjusterRatio * n.getSwimIntensityTime()));
        n.setBikeIntensityTime((n.getBikeIntensityTime() + trainingPlanAdjusterRatio * n.getBikeIntensityTime()));
        n.setRunIntensityTime((n.getRunIntensityTime() + trainingPlanAdjusterRatio * n.getRunIntensityTime()));
        n.setBikeDistance(n.getBikeDistance() + trainingPlanAdjusterRatio * n.getBikeDistance());
        n.setSwimDistance(n.getSwimDistance() + trainingPlanAdjusterRatio * n.getSwimDistance());
        n.setRunDistance(n.getRunDistance() + trainingPlanAdjusterRatio * n.getRunDistance());
    }

}
