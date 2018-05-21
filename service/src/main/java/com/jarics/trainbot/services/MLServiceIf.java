package com.jarics.trainbot.services;

import io.swagger.client.model.SummaryActivity;

import java.util.List;

public interface MLServiceIf {
    MLClasses classify(List<SummaryActivity> pActivities);
}
