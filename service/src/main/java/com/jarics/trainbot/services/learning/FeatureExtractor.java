package com.jarics.trainbot.services.learning;

import com.jarics.trainbot.entities.AthleteActivity;
import com.jarics.trainbot.entities.AthletesFeatures;
import io.swagger.client.model.SummaryActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * The features we extract from raw data are the following:
 * CTL: normal (), overtrained(), undertrained()
 * TSS, ATL, CTL and TSB
 */
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
    private double getCtl(List<Double> pTss) {
        return getWeightedAvg(pTss, 42);
    }

    /**
     * Returns the weighted average of pDays
     *
     * @param pTss
     * @param pDays
     * @return
     */
    private double getWeightedAvg(List<Double> pTss, int pDays) {
        if (pTss.size() < pDays) return 0;
        double lastDaysTss = 0.0;
        for (int i = pTss.size() - 1; i > pTss.size() - pDays; i--) {
            lastDaysTss = lastDaysTss + pTss.get(i);
        }
        return lastDaysTss / pDays;
    }


    /**
     * An exponentially weighted average of your training stress scores from the past 7 days which provides
     * an estimate of your fatigue accounting for the workouts you have done recently. [1] fatigue
     *
     * @return
     * @param pTss
     */
    private double getAtl(List<Double> pTss) {
        return getWeightedAvg(pTss, 7);
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
     * Return the TSS for a single session.
     *
     * NGP = 2399 sec / 5982.9 meters --> 40 min / 6 km --> 40/6 --> 6.66min/km --> 2399/5982.9 --> 0.40sec/meter
     *
     * TSS = [(s x NP/NGP x IF) / (FTP x 3,600)] x 100 [1] Where “s” is duration of the workout
     * in seconds and “3600” is the number of seconds in an hour.
     * @param s workout is seconds (elapse time)
     * @param NP is average watts or average weighted watt (mutually exclusive to NPG)
     * @param NGP normalized graded pace for running (mutually exclusive to NP). Means: moving _time/distance
     * @param IF NGP/FTP  (% of FTP). Ex.: 0.40sec/meter divBy 0.32sec/meter --> 1.25
     * @return TSS
     */
    private double getTss(int s, double NP, double NGP, double IF, double pFtp) {
        //TODO Need to calculate tss differently when bike, run swim...
        double wNpOrNgp = (NP == 0.0 ? NGP : NP);
        double wRet = ((s * wNpOrNgp * IF) / (pFtp * 3600)) * 100;
        return wRet;
    }

    //TODO follow https://docs.google.com/spreadsheets/d/17WGSUFfcUFhNv2n1zmpJHLfjQ89PmMLnhfJaOywIbmU/edit?usp=sharing
    public AthletesFeatures extract(List<AthleteActivity> pActivities, double pSwimFtp, double pBikeFtp, double pRunFtp) {
        List<Double> wTssValues = new ArrayList<>();
        AthletesFeatures wAthletesFeatures = new AthletesFeatures();
        for (AthleteActivity activity : pActivities) {
            double wCurrentFtp;
            double wNPG = 0;
            double IF = 0;
            double wNp = 0;
            switch (activity.getType()) {
                case SWIM: {
                    wCurrentFtp = pSwimFtp;
                    wNPG = activity.getMovingTime() / activity.getDistance();
                    IF = wNPG / wCurrentFtp;
                    wTssValues.add(getTss(activity.getElapsedTime(), wNp, wNPG, IF, wCurrentFtp));
                    break;
                }
                case BIKE: {
                    wCurrentFtp = pBikeFtp;
                    wNp = activity.getWeigthedAvgWatts();
                    wNPG = activity.getMovingTime() / activity.getDistance();
                    IF = wNp / wCurrentFtp;
                    wTssValues.add(getTss(activity.getElapsedTime(), wNp, wNPG, IF, wCurrentFtp));
                    break;
                }
                case RUN: {
                    wCurrentFtp = pRunFtp;
                    wNPG = activity.getMovingTime() / activity.getDistance();
                    IF = wNPG / wCurrentFtp;
                    wTssValues.add(getTss(activity.getElapsedTime(), wNp, wNPG, IF, wCurrentFtp));
                    break;
                }
            }

        }
        double wAtl = getAtl(wTssValues);
        double wCtl = getCtl(wTssValues);
        wAthletesFeatures.setaTL(wAtl);
        wAthletesFeatures.setcTL(wCtl);
        wAthletesFeatures.settSB(wCtl - wAtl);
        return wAthletesFeatures;
    }


}
