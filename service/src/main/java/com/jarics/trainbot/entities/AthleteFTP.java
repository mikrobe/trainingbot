package com.jarics.trainbot.entities;

import com.jarics.trainbot.services.AthletesRanking;
import com.jarics.trainbot.services.EventTypes;
import com.jarics.trainbot.services.MLClasses;
import lombok.EqualsAndHashCode;
import org.dizitart.no2.objects.Id;

@EqualsAndHashCode
public class AthleteFTP {
    @Id
    private long id;
    private double bikeFtp;
    private double runFtp;
    private double swimFtp;
    private EventTypes eventType = EventTypes.olympic;
    private AthletesRanking athletesRanking = AthletesRanking.beginner;
    private MLClasses classification = MLClasses.unknown;
    private String username;

    public double getBikeFtp() {
        return bikeFtp;
    }

    public void setBikeFtp(double bikeFtp) {
        this.bikeFtp = bikeFtp;
    }

    public double getRunFtp() {
        return runFtp;
    }

    public void setRunFtp(double runFtp) {
        this.runFtp = runFtp;
    }

    public double getSwimFtp() {
        return swimFtp;
    }

    public void setSwimFtp(double swimFtp) {
        this.swimFtp = swimFtp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MLClasses getClassification() {
        return classification;
    }

    public void setClassification(MLClasses classification) {
        this.classification = classification;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AthletesRanking getAthletesRanking() {
        return athletesRanking;
    }

    public void setAthletesRanking(AthletesRanking athletesRanking) {
        this.athletesRanking = athletesRanking;
    }

    public EventTypes getEventType() {
        return eventType;
    }

    public void setEventType(EventTypes eventType) {
        this.eventType = eventType;
    }
}



