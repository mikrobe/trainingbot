package com.jarics.trainbot;

import com.jarics.trainbot.entities.AthleteActivity;
import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.services.AthleteRepositoryService;
import com.jarics.trainbot.services.StravaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
//@AutoConfigureMockMvc
public class StravaServiceTests {
    @Autowired private StravaService stravaService;
    @Autowired private AthleteRepositoryService athleteRepositoryService;
    @Autowired private MockMvc mockMvc;
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
