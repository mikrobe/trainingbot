package com.jarics.trainbot.entities;

public class AthleteActivity {
    int weekNbr;
    BotActivityType type;
    int elapsedTime;
    int movingTime;
    float distance;
    double weigthedAvgWatts;
    double pace;

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

    public double getPace() {
        return pace;
    }

    public void setPace(double pace) {
        this.pace = pace;
    }

    public int getWeekNbr() {
        return weekNbr;
    }

    public void setWeekNbr(int weekNbr) {
        this.weekNbr = weekNbr;
    }

    public static String toHeaderString() {
        return "week, " +
                "type," +
                "elapsedTime," +
                "movingTime," +
                "distance," +
                "weigthedAvgWatts," +
                "pace\r\n";
    }

    public String toCsvString() {
        return weekNbr + "," +
                type + "," +
                elapsedTime + "," +
                movingTime + "," +
                distance + "," +
                weigthedAvgWatts + "," +
                pace + "\r\n";
    }
}
