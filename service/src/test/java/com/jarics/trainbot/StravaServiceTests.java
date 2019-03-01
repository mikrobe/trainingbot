package com.jarics.trainbot;

import com.jarics.trainbot.entities.AthleteActivity;
import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.services.AthleteRepositoryService;
import com.jarics.trainbot.services.StravaService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class StravaServiceTests {
    @Autowired private StravaService stravaService;
    @Autowired private AthleteRepositoryService athleteRepositoryService;
    private String trainingBotUserName = "eaudet";

    /**
     * Make sure you athlete is created before running this test
     */
    //    @Test
    public void testGetActivities() {
        AthleteFTP athleteFTP = athleteRepositoryService.findAthleteFtpByUsername(trainingBotUserName);
        List<AthleteActivity> activities = stravaService.getAthleteActivities(athleteFTP, 45);
        for (AthleteActivity activity : activities) {
            System.out.println(activity.toCsvString());
        }
    }

}
