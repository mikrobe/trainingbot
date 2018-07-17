package com.jarics.trainbot.services;

import java.util.Random;

public class OverTrainingGenerator extends TrainingBotService {
    double range = 0.10;

    private double getShortIncreaseTable(int i) {
        return gen(i, shortIncreaseTable);
    }

    private double getLongIncreaseTable(int i) {
        return gen(i, longIncreaseTable);
    }

    private double gen(int i, double[] values) {
        Random wRand = new Random();
        double wSeed = values[i];
        double wLow = wSeed + range;
        double wHigh = wLow + range;
        return wRand.doubles(wLow, wHigh).findFirst().getAsDouble();
    }
}
