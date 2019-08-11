package com.jarics.trainbot.plan;

import java.util.Random;

public class RunningOverPlanBuilder extends RunningPlanBuilder {
    double range = 0.10;

    public double gen(double seed) {
        Random wRand = new Random();
        double wLow = seed + (seed * range);
        double wHigh = wLow + (seed * range);
        return wRand
          .doubles(wLow, wHigh)
          .findFirst()
          .getAsDouble();
    }
}
