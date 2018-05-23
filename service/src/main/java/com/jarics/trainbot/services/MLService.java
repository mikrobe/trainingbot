package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.AthleteFTP;
import io.swagger.client.model.SummaryActivity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MLService implements MLServiceIf {
    @Override
    public MLClasses classify(AthleteFTP pAthleteFTP, List<SummaryActivity> pActivities) {
        GradientDescent wGradientDescent = new GradientDescent();
        return MLClasses.normal;
    }
}
