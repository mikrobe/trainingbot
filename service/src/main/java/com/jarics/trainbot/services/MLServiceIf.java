package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.AthletesFeatures;

import java.util.List;

public interface MLServiceIf {
    MLClasses classify(AthleteFTP pAthleteFTP, List<AthletesFeatures> pAthletesFeatures);
}
