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
import java.util.List;

@Component
public class StravaService {


    /**
     * @param pAthleteFTP
     * @return
     * @throws Exception
     * @deprecated
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
        LocalDate today = LocalDate.now();
        LocalDate elapseDays = today.minus(pElapseDays, ChronoUnit.DAYS);
        ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
        Integer before = new Long(today.atStartOfDay(zoneId).toEpochSecond()).intValue();
        Integer after = new Long(elapseDays.atStartOfDay(zoneId).toEpochSecond()).intValue();
        Integer page = null;
        Integer perPage = null;
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

}
