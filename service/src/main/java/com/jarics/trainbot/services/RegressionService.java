package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.AthleteFTP;
import io.swagger.client.model.SummaryActivity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegressionService implements MLServiceIf {
    @Override
    public MLClasses classify(AthleteFTP pAthleteFTP, List<SummaryActivity> pActivities) {

        return MLClasses.normal;
    }
}
