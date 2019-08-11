package com.jarics.trainbot;

import com.jarics.trainbot.com.jarics.trainbot.utils.JsonUtil;
import com.jarics.trainbot.entities.AthleteActivity;
import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.AthletesFeatures;
import com.jarics.trainbot.plan.Plan;
import com.jarics.trainbot.services.AthleteRepositoryService;
import com.jarics.trainbot.services.MLClasses;
import com.jarics.trainbot.services.PlanService;
import com.jarics.trainbot.services.StravaService;
import com.jarics.trainbot.services.learning.ActivitiesGenerator;
import com.jarics.trainbot.services.learning.FeatureExtractor;
import com.jarics.trainbot.services.learning.GenerateTrainingDataset;
import com.jarics.trainbot.services.learning.WekaMLService;
import com.jarics.trainbot.services.sessions.PlanManager;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @Autowired private PlanManager sessionManager;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WekaMLService wekaMLService;

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
        //        MLClasses mlClasses = wekaMLService.classify(1.0511188630639134E-7, 2.0750096254564565E-7, 1.023890762392543E-7);
        MLClasses mlClasses = wekaMLService.classify(6.461005180496485E-4, 0.002280756384819742, 0.0016346558667700937);
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

        PlanService planService = new PlanService();
        GenerateTrainingDataset generateTrainingDataset = new GenerateTrainingDataset();
        ActivitiesGenerator activitiesGenerator = new ActivitiesGenerator();

        AthleteFTP wAthleteFTP = generateTrainingDataset.generateAthlete();
        Plan plan = planService.getTriathlonPlan(wAthleteFTP);
        List<AthleteActivity> activities = generateTrainingDataset.generateActivities(plan, activitiesGenerator);

        FeatureExtractor wFeatureExtractor = new FeatureExtractor();
        AthletesFeatures wAthletesFeatures = wFeatureExtractor.extract(activities, 101.0, 228.0, 345.0);
        wAthletesFeatures.setAthlete(wAthleteFTP);

        MLClasses mlClasse = wekaMLService.classify(wAthletesFeatures.gettSB(), wAthletesFeatures.getcTL(), wAthletesFeatures.getaTL());
        wAthleteFTP.setClassification(mlClasse);

        Assert.assertEquals(MLClasses.normal, mlClasse);

    }

    @Test
    public void typical_stressedout_guy_overtrained() throws Exception {

        PlanService planService = new PlanService();
        GenerateTrainingDataset generateTrainingDataset = new GenerateTrainingDataset();
        ActivitiesGenerator activitiesGenerator = new ActivitiesGenerator();

        AthleteFTP wAthleteFTP = generateTrainingDataset.generateAthlete();
        Plan plan = planService.getTriathlonOverTrainingPlan(wAthleteFTP);
        List<AthleteActivity> activities = generateTrainingDataset.generateActivities(plan, activitiesGenerator);

        FeatureExtractor wFeatureExtractor = new FeatureExtractor();
        AthletesFeatures wAthletesFeatures = wFeatureExtractor.extract(activities, 101.0, 228.0, 345.0);
        wAthletesFeatures.setAthlete(wAthleteFTP);

        MLClasses mlClasse = wekaMLService.classify(wAthletesFeatures.gettSB(), wAthletesFeatures.getcTL(), wAthletesFeatures.getaTL());
        wAthleteFTP.setClassification(mlClasse);

        Assert.assertEquals(MLClasses.overtrained, mlClasse);
    }

    @Test
    public void typical_lasy_guy_overtrained_with_very_high_but_few_intense_workouts() throws Exception {

        PlanService planService = new PlanService();
        GenerateTrainingDataset generateTrainingDataset = new GenerateTrainingDataset();
        ActivitiesGenerator activitiesGenerator = new ActivitiesGenerator();

        AthleteFTP wAthleteFTP = generateTrainingDataset.generateAthlete();
        Plan plan = planService.getTriathlonOverTrainingPlan(wAthleteFTP);
        List<AthleteActivity> activities = generateTrainingDataset.generateActivities(plan, activitiesGenerator);

        //remove 50% of the activities each weeks. We have tipically swim, bike, run (intensity and volume) equals 3 sessions per week.
        //our guy only does the intensity one and skips sometimes one or more sessions.

        //TODO i wish to randomly remove 20% of items in an array
        List<AthleteActivity> newActivities = new ArrayList<>(activities);

        int i = 0;
        for (AthleteActivity activity : activities) {
            int modulo = i % 4;
            if (modulo == 0) {
                newActivities.remove(activity);
            }
            ++i;
        }

        FeatureExtractor wFeatureExtractor = new FeatureExtractor();
        AthletesFeatures wAthletesFeatures = wFeatureExtractor.extract(newActivities, 101.0, 228.0, 345.0);
        wAthletesFeatures.setAthlete(wAthleteFTP);

        MLClasses mlClasse = wekaMLService.classify(wAthletesFeatures.gettSB(), wAthletesFeatures.getcTL(), wAthletesFeatures.getaTL());
        wAthleteFTP.setClassification(mlClasse);

        Assert.assertEquals(MLClasses.overtrained, mlClasse);
    }

    private int randomBetween(int a, int b, int not) {
        int result = 0;
        while (result == not) {
            Random r = new Random();
            int low = a;
            int high = b;
            result = r.nextInt(high - low) + low;
        }
        return result;
    }


    @Test
    public void plan_must_lower_distances_and_time_at_ftp_for_overtrained() throws Exception {
        PlanService planService = new PlanService();
        GenerateTrainingDataset generateTrainingDataset = new GenerateTrainingDataset();
        ActivitiesGenerator activitiesGenerator = new ActivitiesGenerator();

        AthleteFTP wAthleteFTP = generateTrainingDataset.generateAthlete();
        Plan plan = planService.getTriathlonOverTrainingPlan(wAthleteFTP);
        List<AthleteActivity> activities = generateTrainingDataset.generateActivities(plan, activitiesGenerator);

        FeatureExtractor wFeatureExtractor = new FeatureExtractor();
        AthletesFeatures wAthletesFeatures = wFeatureExtractor.extract(activities, 101.0, 228.0, 345.0);
        wAthletesFeatures.setAthlete(wAthleteFTP);

        MLClasses mlClasse = wekaMLService.classify(wAthletesFeatures.gettSB(), wAthletesFeatures.getcTL(), wAthletesFeatures.getaTL());
        wAthleteFTP.setClassification(mlClasse);

        Assert.assertEquals(MLClasses.overtrained, mlClasse);

        Double savedDistance = plan
          .getPlannedWeeks()
          .get(7)
          .getBikeVolumeSession()
          .getTargetDistance();
        sessionManager.reduceLoad(plan);
        Assert.assertTrue(plan
          .getPlannedWeeks()
          .get(7)
          .getBikeVolumeSession()
          .getTargetDistance() < savedDistance);
    }

    @Test
    public void plan_must_higher_distances_and_time_at_ftp_for_undertrained() throws Exception {
        PlanService planService = new PlanService();
        GenerateTrainingDataset generateTrainingDataset = new GenerateTrainingDataset();
        ActivitiesGenerator activitiesGenerator = new ActivitiesGenerator();

        AthleteFTP wAthleteFTP = generateTrainingDataset.generateAthlete();
        Plan plan = planService.getTriathlonUnderTrainingPlan(wAthleteFTP);
        List<AthleteActivity> activities = generateTrainingDataset.generateActivities(plan, activitiesGenerator);

        FeatureExtractor wFeatureExtractor = new FeatureExtractor();
        AthletesFeatures wAthletesFeatures = wFeatureExtractor.extract(activities, 101.0, 228.0, 345.0);
        wAthletesFeatures.setAthlete(wAthleteFTP);

        MLClasses mlClasse = wekaMLService.classify(wAthletesFeatures.gettSB(), wAthletesFeatures.getcTL(), wAthletesFeatures.getaTL());
        wAthleteFTP.setClassification(mlClasse);

        Assert.assertEquals(MLClasses.undertrained, mlClasse);

        Double savedDistance = plan
          .getPlannedWeeks()
          .get(7)
          .getBikeVolumeSession()
          .getTargetDistance();
        sessionManager.increaseLoad(plan);
        Assert.assertTrue(plan
          .getPlannedWeeks()
          .get(7)
          .getBikeVolumeSession()
          .getTargetDistance() > savedDistance);
    }
}
