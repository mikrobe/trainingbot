package com.jarics.trainbot.plan;

import java.util.Random;

public class BikingOverPlanBuilder extends BikingPlanBuilder {

    double range = 0.10;

    protected double gen(double seed) {
        Random wRand = new Random();
        double wLow = seed + (seed * range);
        double wHigh = wLow + (seed * range);
        return wRand
          .doubles(wLow, wHigh)
          .findFirst()
          .getAsDouble();
    }
}
