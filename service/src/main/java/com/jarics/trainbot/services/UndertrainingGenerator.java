package com.jarics.trainbot.services;

import java.util.Random;


/**
 * seed = 0.1  range between 0 to 0.1
 */
public class UndertrainingGenerator extends TrainingBotService {
    double range = 0.10;

    protected double getShortIncreaseTable(int i) {
        return gen(i, shortIncreaseTable);
    }

    protected double getLongIncreaseTable(int i) {
        return gen(i, longIncreaseTable);
    }

    private double gen(int i, double[] values) {
        Random wRand = new Random();
        double wSeed = values[i];
        double wHigh = wSeed - range;
        double wLow = wHigh - range;
        return wRand.doubles(wLow, wHigh).findFirst().getAsDouble();
    }
}
