package com.jarics.trainbot;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.AthletesFeatures;
import com.jarics.trainbot.services.MLClasses;
import com.jarics.trainbot.services.learning.WekaMLService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class MLTest {

    //MockMcv workaround for buggy spring context load. See: https://stackoverflow.com/questions/42693789/spring-boot-integration-tests-autoconfiguremockmvc-and-context-caching
    static {
        System.setProperty("testing", "true");
    }

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WekaMLService wekaMLService;

    @Value("${strava.user.name}")
    private String stravaUserName;

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
        athleteFTP.setUsername(stravaUserName);
        athletesFeatures.setAthlete(athleteFTP);
        System.out.println(TestUtil.convertObjectToJsonBytes(athletesFeatures));
        MvcResult result = mockMvc.perform(post("/api/ml").contentType(contentType).
                content(TestUtil.convertObjectToJsonBytes(athletesFeatures)))
                .andReturn();
        AthleteFTP athleteFTP1 = (AthleteFTP) TestUtil.convertJsonBytesToObject(result.getResponse().getContentAsString(), AthleteFTP.class);
        Assert.assertEquals(MLClasses.undertrained, athleteFTP1.getClassification());
    }
}
