package com.jarics.trainbot.services.sessions;

import com.jarics.trainbot.entities.SimpleSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SessionManager {

    @Value("${training.plan.reducer.ratio}") private double trainingPlanReducerRatio;

    @Value("${training.plan.increase.ratio}") private double trainingPlanIncreaseRatio;

    public void reduceLoad(List<SimpleSession> sessions) {
        sessions.forEach((n) -> reduceSession(n));
    }

    private void reduceSession(SimpleSession n) {
        n.setTimeAtFtp(n.getTimeAtFtp() - trainingPlanReducerRatio * n.getTimeAtFtp());
        n.setBikeDistance(n.getBikeDistance() - trainingPlanReducerRatio * n.getBikeDistance());
        n.setSwimDistance(n.getSwimDistance() - trainingPlanReducerRatio * n.getSwimDistance());
        n.setRunDistance(n.getRunDistance() - trainingPlanReducerRatio * n.getRunDistance());
    }

    public void increaseLoad(List<SimpleSession> sessions) {
        sessions.forEach((n) -> increaseSession(n));
    }

    private void increaseSession(SimpleSession n) {
        n.setTimeAtFtp(n.getTimeAtFtp() + trainingPlanReducerRatio * n.getTimeAtFtp());
        n.setBikeDistance(n.getBikeDistance() + trainingPlanReducerRatio * n.getBikeDistance());
        n.setSwimDistance(n.getSwimDistance() + trainingPlanReducerRatio * n.getSwimDistance());
        n.setRunDistance(n.getRunDistance() + trainingPlanReducerRatio * n.getRunDistance());
    }

}
