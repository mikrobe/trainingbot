package com.jarics.trainbot;

import com.jarics.trainbot.services.learning.GenerateTrainingDataset;
import org.junit.After;
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
    private GenerateTrainingDataset generateTrainingDataset;

    @Before
    public void before() {

    }

    @After
    public void after() {
    }

    @Test
    public void testMLPC() throws Exception {
        generateTrainingDataset.generate(false);
    }
}
