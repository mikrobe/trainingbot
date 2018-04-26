package com.jarics.trainbot.entities;

public class Effort {
    public Effort(double duration, String instruction) {
        this.duration = duration;
        this.instruction = instruction;
    }

    public Effort(double duration) {
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    private double duration;
    private String instruction;

}
