package com.jarics.trainbot.services.classification;

import com.jarics.trainbot.entities.AthleteActivity;
import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.AthletesFeatures;
import com.jarics.trainbot.services.AthleteRepositoryService;
import com.jarics.trainbot.services.MLClasses;
import com.jarics.trainbot.services.learning.FeatureExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClassificationService {

    @Autowired
    StravaService stravaService;
    @Autowired
    RegressionService mlService;

    @Autowired
    AthleteRepositoryService athleteRepositoryService;

    private void classify(AthleteFTP wAthleteFTP) {
        FeatureExtractor wFeatureExtractor = new FeatureExtractor();
        try {
            //get raw data
            List<AthleteActivity> wActivities = stravaService.getAthleteActivities(wAthleteFTP, 45);
            //create machine learning features based on raw data

            AthletesFeatures wAthletesFeatures =
                    wFeatureExtractor.extract(
                            wActivities,
                            wAthleteFTP.getSwimFtp(),
                            wAthleteFTP.getBikeFtp(),
                            wAthleteFTP.getRunFtp());
            MLClasses wMlClasses = mlService.classify(wAthleteFTP, wAthletesFeatures);
            wAthleteFTP.setClassification(wMlClasses);
            athleteRepositoryService.updateAthleteFTP(wAthleteFTP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
