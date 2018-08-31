package com.jarics.trainbot.services.sessions;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.SimpleSession;

import java.util.List;

public interface TrainingPlanServiceIf {
    List<SimpleSession> getSessions(AthleteFTP pAthleteFTP, int nbrWeeks);
}
