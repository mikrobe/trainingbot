package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.AthleteFTP;
import io.swagger.client.model.SummaryActivity;

import java.util.List;

public interface MLServiceIf {
    MLClasses classify(AthleteFTP pAthleteFTP, List<SummaryActivity> pActivities);
}
