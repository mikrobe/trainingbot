package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.AthleteFTP;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.auth.OAuth;
import io.swagger.client.model.SummaryActivity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class StravaService {


    /**
     * @param pAthleteFTP
     * @return
     * @throws Exception
     */
    public List<SummaryActivity> _getAthleteActivities(AthleteFTP pAthleteFTP, int pElapseDays) throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate elapseDays = today.minus(pElapseDays, ChronoUnit.DAYS);
        ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
        Integer before = new Long(today.atStartOfDay(zoneId).toEpochSecond()).intValue();
        Integer after = new Long(elapseDays.atStartOfDay(zoneId).toEpochSecond()).intValue();
        Integer page = null;
        Integer perPage = null;
        ActivitiesApi api = new ActivitiesApi();
        api.setApiClient(new ApiClient());
        api.getApiClient().setAccessToken("eb701aac06d7542441c4857226e9939bac3aa3a1");
        return api.getLoggedInAthleteActivities(before, after, page, perPage);
    }

    public List<SummaryActivity> getAthleteActivities(AthleteFTP pAthleteFTP, int pElapseDays) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: strava_oauth
        OAuth strava_oauth = (OAuth) defaultClient.getAuthentication("strava_oauth");
        strava_oauth.setAccessToken("eb701aac06d7542441c4857226e9939bac3aa3a1");

        ActivitiesApi apiInstance = new ActivitiesApi();
        Integer before = 56; // Integer | An epoch timestamp to use for filtering activities that have taken place before a certain time.
        Integer after = 56; // Integer | An epoch timestamp to use for filtering activities that have taken place after a certain time.
        Integer page = 56; // Integer | Page number.
        Integer perPage = 30; // Integer | Number of items per page. Defaults to 30.
        try {
            List<SummaryActivity> result = apiInstance.getLoggedInAthleteActivities(before, after, page, perPage);
            System.out.println(result);
            return result;
        } catch (ApiException e) {
            System.err.println("Exception when calling ActivitiesApi#getLoggedInAthleteActivities");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fitness (CTL) is an exponentially weighted average of your last 42 days of training
     * stress scores (TSS) and reflects the training you have done over the last 6 weeks.
     * However, the workouts you did 15 days ago will impact your Fitness more than
     * the workouts you did 30 days ago. [1] Fitness, if you want to look at it that way
     *
     * @return
     */
    public double getCtl(AthleteFTP pAthleteFTP) throws Exception {
        List<SummaryActivity> wActivities = getAthleteActivities(pAthleteFTP, 42);
        List<Double> TSSs = new ArrayList<Double>();
        double tssTotal = 0;
        for (Iterator<SummaryActivity> i = wActivities.iterator(); i.hasNext(); ) {
            tssTotal = tssTotal + getTss(i.next());
        }
        //TODO Should weight in the last 15 days....
        return tssTotal / wActivities.size();
    }

    /**
     * An exponentially weighted average of your training stress scores from the past 7 days which provides
     * an estimate of your fatigue accounting for the workouts you have done recently. [1] fatigue
     *
     * @return
     */
    public double getAtl() {
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
    public double getTsb() {
        return 0;
    }

    /**
     * TSS = [(s x NP/NGP x IF) / (FTP x 3,600)] x 100 [1] Where “s” is duration of the workout
     * in seconds and “3600” is the number of seconds in an hour.
     *
     * @param summaryActivity
     * @return
     */
    public double getTss(SummaryActivity summaryActivity) {
        double s = summaryActivity.getMovingTime();
//        double NP = summaryActivity.
        double NGP;
        double FTP;
        return 0;
    }
}
