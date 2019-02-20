package com.jarics.trainbot.services.learning;

import com.jarics.trainbot.entities.AthleteActivity;
import com.jarics.trainbot.entities.AthletesFeatures;
import com.jarics.trainbot.entities.BotActivityType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FeatureExtractorTest {

    @Test
    public void extract() {
        List<AthleteActivity> activities = new ArrayList<>();
        activities.add(new AthleteActivity(0, BotActivityType.SWIM, 1962, 1962, 1612.9f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.RUN, 2523, 2523, 0.0f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.RUN, 2514, 2443, 4893.6f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.SWIM, 2098, 2098, 1582.4f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.BIKE, 3643, 3612, 34013.4f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.RUN, 2531, 2531, 0.0f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.SWIM, 1938, 1938, 1575.0f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.BIKE, 3899, 3761, 35193.2f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.RUN, 2400, 2400, 4600.0f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.SWIM, 1954, 1954, 1500.0f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.BIKE, 3251, 2786, 19643.7f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.RUN, 2684, 2684, 0.0f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.SWIM, 1586, 1586, 1000.0f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.BIKE, 2001, 2001, 19207.5f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.BIKE, 1354, 1308, 12486.6f, 0.0, 0.0));
        activities.add(new AthleteActivity(0, BotActivityType.BIKE, 3637, 3604, 33557.7f, 0.0, 0.0));
        FeatureExtractor wFeatureExtractor = new FeatureExtractor();
        AthletesFeatures extract = wFeatureExtractor.extract(activities, 100, 237, 100);
        System.out.println(extract.toString());
    }

}
