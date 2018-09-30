package com.jarics.trainbot;

import com.jarics.trainbot.entities.AthletesFeatures;
import com.jarics.trainbot.services.MLClasses;
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

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
    public void testCreateAthleteTwoTimes() throws Exception {

        mockMvc.perform(post("/api/ml").contentType(contentType).
                content(TestUtil.convertObjectToJsonBytes(new AthletesFeatures(1.0511188630639134E-7, 2.0750096254564565E-7, 1.023890762392543E-7))))
                .andExpect(content().string("\"undertrained\""))
                .andDo(print());

    }
}
