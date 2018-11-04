package com.jarics.trainbot.controllers;


import com.jarics.trainbot.com.jarics.trainbot.utils.JsonUtil;
import com.jarics.trainbot.entities.AccessToken;
import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.AthletesFeatures;
import com.jarics.trainbot.entities.SimpleSession;
import com.jarics.trainbot.services.AthleteRepositoryService;
import com.jarics.trainbot.services.MLClasses;
import com.jarics.trainbot.services.StravaService;
import com.jarics.trainbot.services.learning.WekaMLService;
import com.jarics.trainbot.services.sessions.TrainingPlanService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8081")
public class TrainingBotController {

    AthleteRepositoryService athleteRepositoryService;
    TrainingPlanService trainingPlanService;
    WekaMLService wekaMLService;
    StravaService stravaService;


    @Autowired
    public TrainingBotController(
            AthleteRepositoryService athleteRepositoryService,
        TrainingPlanService trainingPlanService, WekaMLService wekaMLService,
        StravaService stravaService) {
        this.athleteRepositoryService = athleteRepositoryService;
        this.trainingPlanService = trainingPlanService;
        this.wekaMLService = wekaMLService;
        this.stravaService = stravaService;
    }

    @RequestMapping(value = "/athlete/plan/{username}", method = RequestMethod.GET, produces = "application/json")
    public List<SimpleSession> plan(@PathVariable("username") String pUsername) {
        List<SimpleSession> wSessions = null;
        AthleteFTP wAthleteFTP = athleteRepositoryService.findAthleteFtpByUsername(pUsername);
        wSessions = trainingPlanService.getSessions(wAthleteFTP, 20);
        return wSessions;
    }

    @RequestMapping(value = "/athlete/session/{username}", method = RequestMethod.GET, produces = "application/json")
    public SimpleSession session(@PathVariable("username") String pUsername, @RequestParam(value = "week") int week) {
//        return trainingBotService.getSession(pUsername, week);
        //TODO not sure
        return null;
    }

    @RequestMapping(value = "/athlete", method = RequestMethod.POST, produces = "application/json")
    public AthleteFTP createAthlete(@RequestBody AthleteFTP pAthleteFTP) throws Exception {
        return athleteRepositoryService.setAthleteFTP(pAthleteFTP);
    }

    @RequestMapping(value = "/athlete", method = RequestMethod.PATCH, produces = "application/json")
    public AthleteFTP updateAthlete(@RequestBody AthleteFTP pAthleteFTP) throws Exception {
        return athleteRepositoryService.updateAthleteFTP(pAthleteFTP);
    }

    @RequestMapping(value = "/athlete/{username}", method = RequestMethod.GET, produces = "application/json")
    public AthleteFTP getAthlete(@PathVariable("username") String pUsername) {
        return athleteRepositoryService.findAthleteFtpByUsername(pUsername);
    }

    @RequestMapping(value = "/athlete/{username}", method = RequestMethod.DELETE, produces = "application/json")
    public AthleteFTP removeAthlete(@PathVariable("username") String pUsername) {
        return athleteRepositoryService.removeAthlete(pUsername);
    }

    @RequestMapping(value = "/ml", method = RequestMethod.POST, produces = "application/json")
    public AthleteFTP classifyAthlete(@RequestBody AthletesFeatures athletesFeatures) throws Exception {

        MLClasses clazz = wekaMLService.classify(athletesFeatures.gettSB(), athletesFeatures.getcTL(), athletesFeatures.getaTL());
        AthleteFTP athleteFTP = athleteRepositoryService.findAthleteFtpByUsername(athletesFeatures.getAthlete().getUsername());
        athleteFTP.setClassification(clazz);
        return athleteFTP;
    }


    /**
     * This is the callback from strava 1) In browser or in html page from TrainingBot do a get:
     * https://www.strava.com/oauth/authorize?client_id=24819&redirect_uri=http://localhost:8080/api/athlete/auth/&response_type=code&scope=public&approval_prompt=force
     * 2) Step (1) will response to this endpoint 3) This endpoint will post to POST
     * https://www.strava.com/oauth/token See.: https://developers.strava.com/docs/authentication/
     */
    @RequestMapping(value = "/athlete/auth/", method = RequestMethod.GET, produces = "application/json")
    public ModelAndView auth(
        @RequestParam(value = "error", required = false) String error,
        @RequestParam(value = "code") String code,
        @RequestParam(value = "scope") String scope) {
        AccessToken accessToken = null;
        ModelAndView mav = new ModelAndView();
        String accessTokenJson = stravaService.getAccesToken("24819",
            "0d3567dd661754637b9df94f90e3334b283628c4",
            code,
            "authorization_code");
        try {
            accessToken = (AccessToken) JsonUtil
                .convertJsonBytesToObject(accessTokenJson, AccessToken.class);
            //TODO store under username the tokens
//            response.setCookie(cookie);
            mav.setViewName("redirect:/swagger-ui.html");
        } catch (IOException e) {
            e.printStackTrace();
            mav.setViewName("redirect:/errorAccessToken.html");
        }
        return mav;

    }




}
