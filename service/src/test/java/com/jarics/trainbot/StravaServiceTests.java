package com.jarics.trainbot;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.services.StravaService;
import org.junit.Test;


public class StravaServiceTests {

    @Test
    public void testGetSession() {
        StravaService wStravaService = new StravaService();
        AthleteFTP wAthleteFTP = new AthleteFTP();
        wStravaService.getAthleteActivities(wAthleteFTP, 45);
    }
}
