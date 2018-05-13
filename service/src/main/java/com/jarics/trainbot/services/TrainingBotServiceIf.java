package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.SimpleSession;

import java.util.List;

public interface TrainingBotServiceIf {

    /**
     * Return all sessions for all three sports based on athletes FTP
     *
     * @param pAthleteFTP
     * @return
     */
    List<SimpleSession> getSession(AthleteFTP pAthleteFTP);

    /**
     * Returns a single session inside a plan.
     *
     * @param pAthleteFTP
     * @param week
     * @return
     */
    SimpleSession getSession(AthleteFTP pAthleteFTP, int week);

}
