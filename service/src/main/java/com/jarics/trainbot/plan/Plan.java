package com.jarics.trainbot.plan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Plan {
    List<PlannedWeek> plannedWeeks;
    String title;
    Date raceDate;

    public Plan() {
        plannedWeeks = new ArrayList();
    }

    public List<PlannedWeek> getPlannedWeeks() {
        return plannedWeeks;
    }

    public void addPlannedWeek(PlannedWeek plannedWeek) {
        this.plannedWeeks.add(plannedWeek);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(Date raceDate) {
        this.raceDate = raceDate;
    }
}
