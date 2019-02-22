package com.jarics.trainbot;

import com.jarics.trainbot.com.jarics.trainbot.utils.JsonUtil;
import com.jarics.trainbot.entities.AthleteActivity;
import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.AthletesFeatures;
import com.jarics.trainbot.entities.BotActivityType;
import com.jarics.trainbot.services.AthleteRepositoryService;
import com.jarics.trainbot.services.MLClasses;
import com.jarics.trainbot.services.StravaService;
import com.jarics.trainbot.services.learning.FeatureExtractor;
import com.jarics.trainbot.services.learning.WekaMLService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class MLTest {

    @Autowired private AthleteRepositoryService athleteRepositoryService;

    @Autowired private StravaService stravaService;

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
        MLClasses mlClasses = wekaMLService.classify(1.0511188630639134E-7, 2.0750096254564565E-7, 1.023890762392543E-7);
        Assert.assertEquals(MLClasses.undertrained, mlClasses);
    }

    @Test
    public void testMLRestApi() throws Exception {
        AthletesFeatures athletesFeatures =
                new AthletesFeatures(1.0511188630639134E-7, 2.0750096254564565E-7, 1.023890762392543E-7);
        AthleteFTP athleteFTP = new AthleteFTP();
        athleteFTP.setUsername(trainingBotUserName);
        athletesFeatures.setAthlete(athleteFTP);
        System.out.println(JsonUtil.convertObjectToJsonBytes(athletesFeatures));
        MvcResult result = mockMvc.perform(post("/api/ml").contentType(contentType).
                content(JsonUtil.convertObjectToJsonBytes(athletesFeatures)))
                .andReturn();
        AthleteFTP athleteFTP1 = (AthleteFTP) JsonUtil
                .convertJsonBytesToObject(result.getResponse().getContentAsString(),
                        AthleteFTP.class);
        Assert.assertEquals(MLClasses.undertrained, athleteFTP1.getClassification());
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
    public void testNormalAthlete() throws Exception {
        List<AthleteActivity> activities = new ArrayList<>();
        activities.add(new AthleteActivity(0, BotActivityType.SWIM, 1962, 1962, 1612.9f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.RUN, 2523, 2523, 0.0f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.RUN, 2514, 2443, 4893.6f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.SWIM, 2098, 2098, 1582.4f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.BIKE, 3643, 3612, 34013.4f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.RUN, 2531, 2531, 0.0f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.SWIM, 1938, 1938, 1575.0f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.BIKE, 3899, 3761, 35193.2f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.RUN, 2400, 2400, 4600.0f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.SWIM, 1954, 1954, 1500.0f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.BIKE, 3251, 2786, 19643.7f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.RUN, 2684, 2684, 0.0f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.SWIM, 1586, 1586, 1000.0f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.BIKE, 2001, 2001, 19207.5f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.BIKE, 1354, 1308, 12486.6f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.BIKE, 3637, 3604, 33557.7f, 0.0, 0.0));
        FeatureExtractor wFeatureExtractor = new FeatureExtractor();
        AthletesFeatures extract = wFeatureExtractor.extract(activities, 101.0, 228.0, 345.0);
        AthleteFTP athleteFTP = new AthleteFTP();
        athleteFTP.setUsername("normal guy");
        extract.setAthlete(athleteFTP);
        MLClasses mlClasse = wekaMLService.classify(extract.gettSB(), extract.getcTL(), extract.getaTL());
        athleteFTP.setClassification(mlClasse);
        Assert.assertEquals(mlClasse, MLClasses.normal);
        System.out.println(extract.toArffData());
    }
}
