package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.AthletesFeatures;
import io.swagger.client.model.SummaryActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FeatureExtractor {
    List<SummaryActivity> activities;

    /**
     * Fitness (CTL) is an exponentially weighted average of your last 42 days of training
     * stress scores (TSS) and reflects the training you have done over the last 6 weeks.
     * However, the workouts you did 15 days ago will impact your Fitness more than
     * the workouts you did 30 days ago. [1] Fitness, if you want to look at it that way
     *
     * @return
     */
    private double getCtl(AthleteFTP pAthleteFTP) {
        List<Double> TSSs = new ArrayList<Double>();
        double tssTotal = 0;
        for (Iterator<SummaryActivity> i = activities.iterator(); i.hasNext(); ) {
            tssTotal = tssTotal + getTss(i.next());
        }
        //TODO Should weight in the last 15 days....
        return tssTotal / activities.size();
    }

    /**
     * An exponentially weighted average of your training stress scores from the past 7 days which provides
     * an estimate of your fatigue accounting for the workouts you have done recently. [1] fatigue
     *
     * @return
     */
    private double getAtl() {
        return 0;
    }

    /**
     * Training Stress Balance (TSB) or Form represents the balance of training stress.
     * Form (TSB) = Yesterday's Fitness (CTL) - Yesterday's Fatigue (ATL)
     * A positive TSB number means that you would have a good chance of performing
     * well during those 'positive' days, and would suggest that you are both fit and fresh.
     *
     * @return
     */
    private double getTsb() {
        return 0;
    }

    /**
     * TSS = [(s x NP/NGP x IF) / (FTP x 3,600)] x 100 [1] Where “s” is duration of the workout
     * in seconds and “3600” is the number of seconds in an hour.
     *
     * @param summaryActivity
     * @return
     */
    private double getTss(SummaryActivity summaryActivity) {
        double s = summaryActivity.getMovingTime();
//        double NP = summaryActivity.
        double NGP;
        double FTP;
        return 0;
    }

    public List<AthletesFeatures> extract(List<SummaryActivity> wActivities) {
        //TODO calculate
        activities = wActivities;
        List<AthletesFeatures> wAthletesFeaturesList = new ArrayList<>();
        AthletesFeatures wAthletesFeatures = new AthletesFeatures();
        wAthletesFeatures.setATL(0.1);
        wAthletesFeatures.setCTL(0.1);
        wAthletesFeatures.setNGP(0.1);
        wAthletesFeatures.setNP(0.1);
        wAthletesFeatures.setTSB(0.1);
        wAthletesFeatures.setTSS(0.1);
        wAthletesFeatures.setS(2);
        wAthletesFeaturesList.add(wAthletesFeatures);
        return wAthletesFeaturesList;
    }
}
