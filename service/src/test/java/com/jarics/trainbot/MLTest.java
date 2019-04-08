package com.jarics.trainbot;

import com.jarics.trainbot.com.jarics.trainbot.utils.JsonUtil;
import com.jarics.trainbot.entities.AthleteActivity;
import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.AthletesFeatures;
import com.jarics.trainbot.entities.SimpleSession;
import com.jarics.trainbot.services.AthleteRepositoryService;
import com.jarics.trainbot.services.MLClasses;
import com.jarics.trainbot.services.StravaService;
import com.jarics.trainbot.services.learning.*;
import com.jarics.trainbot.services.sessions.SessionManager;
import com.jarics.trainbot.services.sessions.TrainingPlanService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.Charset;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class MLTest {

    @Autowired private AthleteRepositoryService athleteRepositoryService;

    @Autowired private StravaService stravaService;

    @Autowired private TrainingPlanService trainingPlanService;

    @Autowired private SessionManager sessionManager;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WekaMLService wekaMLService;

    @Autowired private OverTrainingGenerator overTrainingGenerator;

    @Autowired private NormalTrainingGenerator normalTrainingGenerator;

    private String trainingBotUserName = "eaudet";

    @Before
    public void before() {

    }

    @After
    public void after() {
    }

    @Test
    public void testMLPC() throws Exception {
        wekaMLService.generate();
        wekaMLService.learn();
        //1.0511188630639134E-7,2.0750096254564565E-7,1.023890762392543E-7,undertrained
        MLClasses mlClasses = wekaMLService.classify(1.0511188630639134E-7, 2.0750096254564565E-7, 1.023890762392543E-7);
        Assert.assertEquals(MLClasses.undertrained, mlClasses);
    }

    @Test
    @WithMockUser(username = "admin", password = "Triat001!")
    public void testMLRestApi() throws Exception {
        String wUrl = "/api/ml/" + trainingBotUserName;
        mockMvc
          .perform(get(wUrl))
          .andExpect(status().isOk())
          .andDo(print());
        MvcResult result = mockMvc
          .perform(get(wUrl))
          .andExpect(status().isOk())
          .andReturn();
        AthleteFTP athleteFTP1 = (AthleteFTP) JsonUtil
                .convertJsonBytesToObject(result.getResponse().getContentAsString(),
                        AthleteFTP.class);
        Assert.assertNotNull(athleteFTP1.getClassification());
    }

    /**
     * @throws Exception
     */
    @Test
    public void testFeatures() throws Exception {
        AthleteFTP athleteFTP = athleteRepositoryService.findAthleteFtpByUsername(trainingBotUserName);
        AthletesFeatures athletesFeatures = stravaService.extractAthletesFeatures(athleteFTP);
        athletesFeatures.setAthlete(athleteFTP);
        System.out.print(athletesFeatures.toArffData());
        MLClasses mlClasse = wekaMLService.classify(athletesFeatures.gettSB(), athletesFeatures.getcTL(), athletesFeatures.getaTL());
        athleteFTP.setClassification(mlClasse);
        System.out.println(athletesFeatures.toArffData());
    }

    @Test
    public void typical_normal_guy_following_normal_plan() throws Exception {
        ActivitiesGenerator activitiesGenerator = new ActivitiesGenerator();
        List<AthleteActivity> normalTrainingActivities;
        AthleteFTP athleteFTP = new AthleteFTP();
        athleteFTP.setUsername("normal trained guy");

        List<SimpleSession> wNormalSessions = trainingPlanService.getSessions(athleteFTP, 20);

        normalTrainingActivities = activitiesGenerator.generateActivities(wNormalSessions);

        FeatureExtractor wFeatureExtractor = new FeatureExtractor();
        AthletesFeatures extract = wFeatureExtractor.extract(normalTrainingActivities, 101.0, 228.0, 345.0);
        extract.setAthlete(athleteFTP);
        MLClasses mlClasse = wekaMLService.classify(extract.gettSB(), extract.getcTL(), extract.getaTL());
        athleteFTP.setClassification(mlClasse);

        Assert.assertEquals(MLClasses.normal, mlClasse);

    }

    @Test
    public void typical_stressedout_guy_undertrained_with_very_high_but_few_intense_workouts() throws Exception {
        ActivitiesGenerator activitiesGenerator = new ActivitiesGenerator();
        List<AthleteActivity> overTrainingActivities;
        AthleteFTP athleteFTP = new AthleteFTP();
        athleteFTP.setUsername("over trained guy");

        List<SimpleSession> wNormalSessions = trainingPlanService.getSessions(athleteFTP, 20);
        double saved = wNormalSessions
          .get(7)
          .getBikeDistance();

        List<SimpleSession> wOverSessions = overTrainingGenerator.getSessions(athleteFTP, 20);
        wOverSessions = wOverSessions.subList(0, 5);

        overTrainingActivities = activitiesGenerator.generateActivities(wOverSessions);

        FeatureExtractor wFeatureExtractor = new FeatureExtractor();
        AthletesFeatures extract = wFeatureExtractor.extract(overTrainingActivities, 101.0, 228.0, 345.0);
        extract.setAthlete(athleteFTP);
        MLClasses mlClasse = wekaMLService.classify(extract.gettSB(), extract.getcTL(), extract.getaTL());
        athleteFTP.setClassification(mlClasse);

        Assert.assertEquals(MLClasses.undertrained, mlClasse);

        sessionManager.reduceLoad(wNormalSessions);
        Assert.assertTrue(wNormalSessions
          .get(7)
          .getBikeDistance() < saved);
    }

    @Test
    public void plan_must_lower_distances_and_time_at_ftp_for_overtrained() throws Exception {
        ActivitiesGenerator activitiesGenerator = new ActivitiesGenerator();
        List<AthleteActivity> overTrainingActivities;
        AthleteFTP athleteFTP = new AthleteFTP();
        athleteFTP.setUsername("over trained guy");

        List<SimpleSession> wNormalSessions = trainingPlanService.getSessions(athleteFTP, 20);
        double saved = wNormalSessions
          .get(7)
          .getBikeDistance();

        List<SimpleSession> wOverSessions = overTrainingGenerator.getSessions(athleteFTP, 20);
        wOverSessions.forEach((n) -> System.out.println(n.toString()));

        overTrainingActivities = activitiesGenerator.generateActivities(wOverSessions);

        FeatureExtractor wFeatureExtractor = new FeatureExtractor();
        AthletesFeatures extract = wFeatureExtractor.extract(overTrainingActivities, 101.0, 228.0, 345.0);
        extract.setAthlete(athleteFTP);
        MLClasses mlClasse = wekaMLService.classify(extract.gettSB(), extract.getcTL(), extract.getaTL());
        athleteFTP.setClassification(mlClasse);

        Assert.assertEquals(MLClasses.overtrained, mlClasse);

        sessionManager.reduceLoad(wNormalSessions);
        Assert.assertTrue(wNormalSessions
          .get(7)
          .getBikeDistance() < saved);
    }
}
