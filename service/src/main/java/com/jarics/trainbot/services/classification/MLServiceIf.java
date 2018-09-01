package com.jarics.trainbot.services.classification;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.AthletesFeatures;
import com.jarics.trainbot.services.MLClasses;

public interface MLServiceIf {
    MLClasses classify(AthleteFTP pAthleteFTP, AthletesFeatures pAthletesFeatures);
}
