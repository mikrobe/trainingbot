package com.jarics.trainbot.entities;

import com.jarics.trainbot.services.MLClasses;
import lombok.EqualsAndHashCode;
import org.dizitart.no2.objects.Id;

@EqualsAndHashCode
public class AthleteFTP {
    private double bikeFtp;
    private double runFtp;
    private double swimFtp;
    private int target; // 0,1,2,3 for ironman, 70.3, olympic, sprint
    @Id
    private long id;
    private MLClasses classification;

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

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
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
}



