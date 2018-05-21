package com.jarics.trainbot.services;

import io.swagger.client.model.SummaryActivity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MLService implements MLServiceIf {
    @Override
    public MLClasses classify(List<SummaryActivity> pActivities) {
        GradientDescent wGradientDescent = new GradientDescent();
        return MLClasses.normal;
    }
}
