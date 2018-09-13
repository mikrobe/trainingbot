package com.jarics.trainbot;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.services.EventTypes;
import org.junit.After;
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
import java.util.UUID;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * PersonController Tester.
 *
 * @author <Erick Audet>
 * @version 1.0
 * @since <pre>Feb 5, 2018</pre>
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class StampControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //MockMcv workaround for buggy spring context load. See: https://stackoverflow.com/questions/42693789/spring-boot-integration-tests-autoconfiguremockmvc-and-context-caching
    static {
        System.setProperty("testing", "true");
    }

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));


    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    @Test
    public void testCreateAthlete() throws Exception {
        AthleteFTP wAthleteFTP = generateAthlete();
        createAthlete(wAthleteFTP);
        AthleteFTP athleteFTP = findAthlete(wAthleteFTP.getUsername());
        deleteAthlete(wAthleteFTP.getUsername());
    }

    @Test
    public void testCreateAthleteTwoTimes() throws Exception {
        AthleteFTP wAthleteFTP = generateAthlete();
        createAthlete(wAthleteFTP);

        try {
            mockMvc.perform(post("/api/athlete").contentType(contentType).
                    content(TestUtil.convertObjectToJsonBytes(wAthleteFTP)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().string("Athlete with same username already exists..."))
                    .andDo(print());
        } catch (Exception e) {
            //sink it
        }

        deleteAthlete(wAthleteFTP.getUsername());
    }

    private void createAthlete(AthleteFTP wAthleteFTP) throws Exception {
        mockMvc.perform(post("/api/athlete").contentType(contentType).content(TestUtil.convertObjectToJsonBytes(wAthleteFTP))).andReturn();
    }

    private void deleteAthlete(String pUsername) throws Exception {
        String wUrl = "/api/athlete/" + pUsername;
        mockMvc.perform(delete(wUrl)).andExpect(status().isOk()).andExpect(content().
                string(org.hamcrest.Matchers.containsString("username\":\"" + pUsername + "\"")));
    }

    private AthleteFTP findAthlete(String pUsername) throws Exception {
        String wUrl = "/api/athlete/" + pUsername;
        MvcResult wMvcResult = mockMvc.perform(get(wUrl)).andExpect(status().isOk()).andExpect(content().
                string(org.hamcrest.Matchers.containsString("username\":\"" + pUsername + "\""))).andReturn();
        AthleteFTP wFtp = (AthleteFTP) TestUtil.convertJsonBytesToObject(wMvcResult.getResponse().getContentAsString(), AthleteFTP.class);
        return wFtp;
    }

    @Test
    public void testChangeAthleteFtp() throws Exception {
        AthleteFTP wAthleteFTP = generateAthlete();
        createAthlete(wAthleteFTP);
        AthleteFTP wUpdated = findAthlete(wAthleteFTP.getUsername());
        wUpdated.setBikeFtp(300.0);
        updateAthlete(wUpdated);
        wUpdated = findAthlete(wAthleteFTP.getUsername());
        if (wUpdated.getBikeFtp() != wUpdated.getBikeFtp()) {
            fail("update fail");
        }
        deleteAthlete(wAthleteFTP.getUsername());
    }

    @Test
    public void testChangeAthleteFtpOnWrongUserName() throws Exception {
        AthleteFTP wAthleteFTP = generateAthlete();
        AthleteFTP wAthleteFTP2 = generateAthlete();
        createAthlete(wAthleteFTP);
        createAthlete(wAthleteFTP2);
        AthleteFTP wUpdated = findAthlete(wAthleteFTP.getUsername());
        wUpdated.setBikeFtp(300.0);
        wUpdated.setUsername(wAthleteFTP2.getUsername());
        //update and handle exception
        try {
            mockMvc.perform(patch("/api/athlete").contentType(contentType).content(TestUtil.convertObjectToJsonBytes(wAthleteFTP)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().string("Illegal update"))
                    .andDo(print());
        } catch (Exception e) {
            //sink it
        }

        deleteAthlete(wAthleteFTP.getUsername());
        deleteAthlete(wAthleteFTP2.getUsername());
    }

    private void updateAthlete(AthleteFTP wAthleteFTP) throws Exception {
        mockMvc.perform(patch("/api/athlete").contentType(contentType).content(TestUtil.convertObjectToJsonBytes(wAthleteFTP)));
    }

    private AthleteFTP generateAthlete() {
        AthleteFTP wAthleteFTP = new AthleteFTP();
        wAthleteFTP.setRunFtp(345.0); //5:45 --> 300+45 = 345
        wAthleteFTP.setBikeFtp(228.0);
        wAthleteFTP.setSwimFtp(101.0);
        wAthleteFTP.setEventType(EventTypes.olympic);
        wAthleteFTP.setUsername(UUID.randomUUID().toString());
        return wAthleteFTP;
    }

    @Test
    public void testGetPlan() throws Exception {
        AthleteFTP wAthleteFTP = generateAthlete();
        createAthlete(wAthleteFTP);
        String wUrl = "/api/athlete/plan" + wAthleteFTP.getUsername();
        mockMvc.perform(get(wUrl)).andExpect(status().isOk());
        deleteAthlete(wAthleteFTP.getUsername());
    }

    @Test
    public void testGetSession() throws Exception {
        AthleteFTP wAthleteFTP = generateAthlete();
        createAthlete(wAthleteFTP);
        String wUrl = "/api/athlete/session" + wAthleteFTP.getUsername();
        mockMvc.perform(get(wUrl).param("week", "4")).andExpect(status().isOk());
        deleteAthlete(wAthleteFTP.getUsername());
    }

}
