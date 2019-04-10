package com.jarics.trainbot;

import com.jarics.trainbot.com.jarics.trainbot.utils.JsonUtil;
import com.jarics.trainbot.entities.Credentials;
import com.jarics.trainbot.entities.LoginResponse;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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
public class SecurityControllerTest {

    @Autowired private MockMvc mockMvc;

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
    public void loginOk() throws Exception {
        MvcResult result = mockMvc
          .perform(post("/api/login")
            .contentType(contentType)
            .content(JsonUtil.convertObjectToJsonBytes(new Credentials("admin", "Triat001!"))))
          .andDo(print())
          .andReturn();
        LoginResponse loginResponse = (LoginResponse) JsonUtil.convertJsonBytesToObject(result
          .getResponse()
          .getContentAsString(), LoginResponse.class);
        Assert.assertNotNull(loginResponse
          .getMessage()
          .contains("Valid"));
    }

    @Test
    public void loginWrongPassword() throws Exception {
        MvcResult result = mockMvc
          .perform(post("/api/login")
            .contentType(contentType)
            .content(JsonUtil.convertObjectToJsonBytes(new Credentials("admin", "Triat001!_"))))
          .andDo(print())
          .andReturn();
        LoginResponse loginResponse = (LoginResponse) JsonUtil.convertJsonBytesToObject(result
          .getResponse()
          .getContentAsString(), LoginResponse.class);
        Assert.assertNotNull(loginResponse
          .getMessage()
          .contains("Invalid"));
    }

    @Test
    public void loginWrongUsername() throws Exception {
        MvcResult result = mockMvc
          .perform(post("/api/login")
            .contentType(contentType)
            .content(JsonUtil.convertObjectToJsonBytes(new Credentials("admin_", "Triat001!"))))
          .andDo(print())
          .andReturn();
        LoginResponse loginResponse = (LoginResponse) JsonUtil.convertJsonBytesToObject(result
          .getResponse()
          .getContentAsString(), LoginResponse.class);
        Assert.assertNotNull(loginResponse
          .getMessage()
          .contains("Invalid"));
    }

}
