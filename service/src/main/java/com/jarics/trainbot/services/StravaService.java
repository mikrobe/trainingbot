package com.jarics.trainbot.services;

import io.swagger.client.ApiClient;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.model.SummaryActivity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class StravaService {


    public void getAthleteActivities() throws Exception {
        //last two weeks
        //Get the current date
        LocalDate today = LocalDate.now();

        //substract 4 week to the current date
        LocalDate nextWeek = today.minus(4, ChronoUnit.WEEKS);
        ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
        Integer before = new Long(today.atStartOfDay(zoneId).toEpochSecond()).intValue();
        Integer after = new Long(nextWeek.atStartOfDay(zoneId).toEpochSecond()).intValue();
        Integer page = null;
        Integer perPage = null;
        ActivitiesApi api = new ActivitiesApi();
        api.setApiClient(new ApiClient());
        api.getApiClient().setAccessToken("eb701aac06d7542441c4857226e9939bac3aa3a1");
        List<SummaryActivity> response = api.getLoggedInAthleteActivities(before, after, page, perPage);
        System.out.println();

    }
}
