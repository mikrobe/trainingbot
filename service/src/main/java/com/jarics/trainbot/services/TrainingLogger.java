package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.AthletesFeatures;

public interface TrainingLogger {
    AthletesFeatures extractAthletesFeatures(AthleteFTP athleteFTP);
}
