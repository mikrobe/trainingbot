package com.jarics.trainbot;

import com.jarics.trainbot.entities.AthleteActivity;
import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.AthletesFeatures;
import com.jarics.trainbot.services.AthleteRepositoryService;
import com.jarics.trainbot.services.MLClasses;
import com.jarics.trainbot.services.StravaService;
import com.jarics.trainbot.services.learning.WekaMLService;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class StravaServiceTests {
    @Autowired
    private StravaService stravaService;
    @Autowired
    private AthleteRepositoryService athleteRepositoryService;
    @Autowired
    private WekaMLService wekaMLService;
    private String stravaUserName = "eaudet";

    @Test
    public void testGetActivities() {
        AthleteFTP athleteFTP = athleteRepositoryService.findAthleteFtpByUsername(stravaUserName);
        List<AthleteActivity> activities = stravaService.getAthleteActivities(athleteFTP, 45);
        for (AthleteActivity activity : activities) {
            System.out.println(activity.toCsvString());
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void testFeatures() throws Exception {
        AthleteFTP athleteFTP = athleteRepositoryService.findAthleteFtpByUsername(stravaUserName);
        AthletesFeatures athletesFeatures = stravaService.extractAthletesFeatures(athleteFTP);
        athletesFeatures.setAthlete(athleteFTP);
        System.out.print(athletesFeatures.toArffData());
        MLClasses mlClasse = wekaMLService.classify(athletesFeatures.gettSB(), athletesFeatures.getcTL(), athletesFeatures.getaTL());
        athleteFTP.setClassification(mlClasse);
        System.out.println(athletesFeatures.toArffData());
    }

}
