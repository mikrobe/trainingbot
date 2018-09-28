package com.jarics.trainbot;

import com.jarics.trainbot.services.MLClasses;
import com.jarics.trainbot.services.learning.WekaMLService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MLTest {

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
}
