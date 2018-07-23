package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.AthleteActivity;
import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.BotActivityType;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.auth.OAuth;
import io.swagger.client.model.ActivityType;
import io.swagger.client.model.DetailedActivity;
import io.swagger.client.model.DetailedSegmentEffort;
import io.swagger.client.model.SummaryActivity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class StravaService {

    public List<AthleteActivity> getAthleteActivities(AthleteFTP pAthleteFTP, int pElapseDays) {
        prepareDefaultClient();
        List<AthleteActivity> wAthleteActivities = new ArrayList<>();
        ActivitiesApi apiInstance = new ActivitiesApi();
        LocalDate today = LocalDate.now();
        LocalDate elapseDays = today.minus(pElapseDays, ChronoUnit.DAYS);
        ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
        Integer before = new Long(today.atStartOfDay(zoneId).toEpochSecond()).intValue();
        Integer after = new Long(elapseDays.atStartOfDay(zoneId).toEpochSecond()).intValue();
        Integer page = null;
        Integer perPage = null;
        try {
            List<SummaryActivity> result = apiInstance.getLoggedInAthleteActivities(before, after, page, perPage);
            for (SummaryActivity wSummaryActivity : result) {
                DetailedActivity wDetailedActivity = getAthleteDetailedActivity(wSummaryActivity.getId());
                AthleteActivity wAthleteActivity = convert(wSummaryActivity);
                if (wSummaryActivity.getType().equals(ActivityType.RIDE)) {
                    wAthleteActivity.setWeigthedAvgWatts(calculateWeightedAvgWatts(wDetailedActivity.getSegmentEfforts()));
                }
                wAthleteActivities.add(wAthleteActivity);
            }
            return wAthleteActivities;
        } catch (ApiException e) {
            System.err.println("Exception when calling ActivitiesApi#getLoggedInAthleteActivities");
            e.printStackTrace();
            return null;
        }
    }

    private double calculateWeightedAvgWatts(List<DetailedSegmentEffort> segmentEfforts) {
        Float wAvgWatts = 0f;
        for (DetailedSegmentEffort wSegment : segmentEfforts) {
            wAvgWatts = wAvgWatts + wSegment.getAverageWatts();
        }
        return wAvgWatts / segmentEfforts.size();
    }

    public DetailedActivity getAthleteDetailedActivity(Long pActivityId) {
        prepareDefaultClient();

        DetailedActivity wDetailedActivity;
        ActivitiesApi apiInstance = new ActivitiesApi();
        try {
            wDetailedActivity = apiInstance.getActivityById(pActivityId, Boolean.TRUE);
        } catch (ApiException e) {
            System.err.println("Exception when calling ActivitiesApi#getLoggedInAthleteActivities");
            e.printStackTrace();
            return null;
        }
        return wDetailedActivity;
    }

    private void prepareDefaultClient() {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        OAuth strava_oauth = (OAuth) defaultClient.getAuthentication("strava_oauth");
        strava_oauth.setAccessToken("eb701aac06d7542441c4857226e9939bac3aa3a1");
    }

    private AthleteActivity convert(SummaryActivity pSummaryActivity) {
        AthleteActivity wAthleteActivity = new AthleteActivity();
        wAthleteActivity.setDistance(pSummaryActivity.getDistance());
        wAthleteActivity.setElapsedTime(pSummaryActivity.getElapsedTime());
        wAthleteActivity.setMovingTime(pSummaryActivity.getMovingTime());
        switch (pSummaryActivity.getType()) {
            case SWIM:
                wAthleteActivity.setType(BotActivityType.SWIM);
                break; // optional

            case RIDE:
                wAthleteActivity.setType(BotActivityType.BIKE);
                break; // optional

            case RUN:
                wAthleteActivity.setType(BotActivityType.RUN);
                break; // optional
        }
        return wAthleteActivity;
    }

}
