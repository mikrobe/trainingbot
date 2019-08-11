package com.jarics.trainbot.plan;

import java.util.Random;

public class RunningUnderPlanBuilder extends RunningPlanBuilder {
    double range = 0.10;

    protected double gen(double seed) {
        Random wRand = new Random();
        double wHigh = seed - (seed * range);
        double wLow = wHigh - (seed * range);
        return wRand
          .doubles(wLow, wHigh)
          .findFirst()
          .getAsDouble();
    }

}
