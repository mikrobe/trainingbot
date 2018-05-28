package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.SimpleSession;

import java.util.List;

public interface TrainingBotServiceIf {

    /**
     * Return all sessions for all three sports based on athletes FTP
     *
     * @return
     */
    List<SimpleSession> getSession(String pUsername);

    /**
     * Returns a single session inside a plan.
     *
     * @param week
     * @return
     */
    SimpleSession getSession(String pUsername, int week);

}
