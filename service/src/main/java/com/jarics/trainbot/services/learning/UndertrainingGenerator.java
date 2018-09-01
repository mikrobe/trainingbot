package com.jarics.trainbot.services.learning;


import com.jarics.trainbot.services.sessions.TrainingPlanService;

import java.util.Random;


/**
 * seed = 0.1  range between 0 to 0.1
 */
public class UndertrainingGenerator extends TrainingPlanService {
    double range = 0.10;

    @Override
    protected double getBaseIntensityIncreaseRatio(int phase) {
        return gen(phase, baseIntensityIncreaseRatio);
    }

    @Override
    protected double getBaseDistanceIncreaseRatio(int phase) {
        return gen(phase, baseDistanceIncreaseRatio);
    }

    private double gen(int i, double[] values) {
        Random wRand = new Random();
        double wSeed = values[i];
        double wHigh = wSeed - range;
        double wLow = wHigh - range;
        return wRand.doubles(wLow, wHigh).findFirst().getAsDouble();
    }
}
