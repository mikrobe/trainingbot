package com.jarics.trainbot.plan;

public class Session {
    double targetDistance;
    double targetIntervalsLowTime;
    double targetIntervalsHighTime;
    double lastFtp;
    double targetHighFtp;
    double targetLowFtp;
    double targetVolumeTime;
    double targetVolumeFtp;

    public Session() {
    }

    public double getTargetDistance() {
        return targetDistance;
    }

    public void setTargetDistance(double targetDistance) {
        this.targetDistance = targetDistance;
    }

    public double getTargetIntervalsLowTime() {
        return targetIntervalsLowTime;
    }

    public void setTargetIntervalsLowTime(double targetIntervalsLowTime) {
        this.targetIntervalsLowTime = targetIntervalsLowTime;
    }

    public double getTargetIntervalsHighTime() {
        return targetIntervalsHighTime;
    }

    public void setTargetIntervalsHighTime(double targetIntervalsHighTime) {
        this.targetIntervalsHighTime = targetIntervalsHighTime;
    }

    public double getLastFtp() {
        return lastFtp;
    }

    public void setLastFtp(double lastFtp) {
        this.lastFtp = lastFtp;
    }

    public double getTargetHighFtp() {
        return targetHighFtp;
    }

    public void setTargetHighFtp(double targetHighFtp) {
        this.targetHighFtp = targetHighFtp;
    }

    public double getTargetLowFtp() {
        return targetLowFtp;
    }

    public void setTargetLowFtp(double targetLowFtp) {
        this.targetLowFtp = targetLowFtp;
    }

    public double getTargetVolumeTime() {
        return targetVolumeTime;
    }

    public void setTargetVolumeTime(double targetVolumeTime) {
        this.targetVolumeTime = targetVolumeTime;
    }

    public double getTargetVolumeFtp() {
        return targetVolumeFtp;
    }

    public void setTargetVolumeFtp(double targetVolumeFtp) {
        this.targetVolumeFtp = targetVolumeFtp;
    }
}
