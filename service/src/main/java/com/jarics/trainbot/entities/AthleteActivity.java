package com.jarics.trainbot.entities;

public class AthleteActivity {
    BotActivityType type;
    int elapsedTime;
    int movingTime;
    float distance;
    double weigthedAvgWatts;

    public void setType(BotActivityType type) {
        this.type = type;
    }

    public BotActivityType getType() {
        return type;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setMovingTime(int movingTime) {
        this.movingTime = movingTime;
    }

    public int getMovingTime() {
        return movingTime;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }

    public void setWeigthedAvgWatts(double weigthedAvgWatts) {
        this.weigthedAvgWatts = weigthedAvgWatts;
    }

    public double getWeigthedAvgWatts() {
        return weigthedAvgWatts;
    }
}
