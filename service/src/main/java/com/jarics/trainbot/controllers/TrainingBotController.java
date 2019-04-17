package com.jarics.trainbot.controllers;

import com.jarics.trainbot.com.jarics.trainbot.utils.JsonUtil;
import com.jarics.trainbot.entities.*;
import com.jarics.trainbot.services.AthleteRepositoryService;
import com.jarics.trainbot.services.MLClasses;
import com.jarics.trainbot.services.StravaService;
import com.jarics.trainbot.services.learning.WekaMLService;
import com.jarics.trainbot.services.sessions.TrainingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TrainingBotController {

    AthleteRepositoryService athleteRepositoryService;
    TrainingPlanService trainingPlanService;
    WekaMLService wekaMLService;
    StravaService stravaService;

    @Autowired
    public TrainingBotController(AthleteRepositoryService athleteRepositoryService, TrainingPlanService trainingPlanService, WekaMLService wekaMLService, StravaService stravaService) {
        this.athleteRepositoryService = athleteRepositoryService;
        this.trainingPlanService = trainingPlanService;
        this.wekaMLService = wekaMLService;
        this.stravaService = stravaService;
    }

    @RequestMapping(value = "/athlete/plan/{username}", method = RequestMethod.GET, produces = "application/json")
    public List<SimpleSession> plan(@PathVariable("username") String pUsername) throws Exception {
        List<SimpleSession> wSessions = null;
        AthleteFTP wAthleteFTP = athleteRepositoryService.findAthleteFtpByUsername(pUsername);
        if (wAthleteFTP != null) {
            wSessions = trainingPlanService.getSessions(wAthleteFTP, 20);
        } else {
            throw new Exception("User does not exist");
        }
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
        AthleteFTP athleteFTP = athleteRepositoryService.createAthleteFTP(pAthleteFTP);
        return athleteFTP;
    }

    @RequestMapping(value = "/athlete", method = RequestMethod.PATCH, produces = "application/json")
    public AthleteFTP updateAthlete(@RequestBody AthleteFTP pAthleteFTP) throws Exception {
        return athleteRepositoryService.updateAthleteFTP(pAthleteFTP);
    }

    @RequestMapping(value = "/athlete/{username}", method = RequestMethod.GET, produces = "application/json")
    public AthleteFTP getAthlete(@PathVariable("username") String pUsername) {

        // security check....can this be achieve without doing this in every method?
        AthleteFTP user = (AthleteFTP) SecurityContextHolder
          .getContext()
          .getAuthentication()
          .getPrincipal();

        return athleteRepositoryService.findAthleteFtpByUsername(pUsername);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public LoginResponse login(@RequestBody Credentials credentials) {
        AthleteFTP athleteFTP = athleteRepositoryService.findAthleteFtpByUsername(credentials.getUsername());
        if (athleteFTP == null || !credentials
          .getPassword()
          .equals(athleteFTP.getPassword())) {
            return new LoginResponse("Invalid username or password");
        }
        return new LoginResponse("Valid credentials");
    }

    @RequestMapping(value = "/athlete/{username}", method = RequestMethod.DELETE, produces = "application/json")
    public AthleteFTP removeAthlete(@PathVariable("username") String pUsername) {
        return athleteRepositoryService.removeAthlete(pUsername);
    }

    @RequestMapping(value = "/ml/{username}", method = RequestMethod.GET, produces = "application/json")
    public AthleteFTP classifyAthlete(@PathVariable("username") String pUsername) throws Exception {
        AthleteFTP athleteFTP = athleteRepositoryService.findAthleteFtpByUsername(pUsername);
        if (athleteFTP == null) {
            throw new Exception("User does not exist");
        }
        AthletesFeatures athletesFeatures = stravaService.extractAthletesFeatures(athleteFTP);
        athletesFeatures.setAthlete(athleteFTP);
        MLClasses mlClasse = wekaMLService.classify(athletesFeatures.gettSB(), athletesFeatures.getcTL(), athletesFeatures.getaTL());
        athleteFTP.setClassification(mlClasse);
        athleteRepositoryService.updateAthleteFTP(athleteFTP);
        return athleteFTP;
    }

    /**
     * This is the callback from strava 1) In browser or in html page from TrainingBot do a get:
     * https://www.strava.com/oauth/authorize?client_id=24819&redirect_uri=http://localhost:8080/api/athlete/auth/&response_type=code&scope=public&approval_prompt=force
     * 2) Step (1) will response to this endpoint 3) This endpoint will post to POST
     * https://www.strava.com/oauth/token See.: https://developers.strava.com/docs/authentication/
     */
    @RequestMapping(value = "/athlete/auth/", method = RequestMethod.GET, produces = "application/json")
    public ModelAndView auth(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "code") String code, @RequestParam(value = "scope") String scope) {
        AccessToken accessToken = null;
        ModelAndView mav = new ModelAndView();
        String accessTokenJson = stravaService.getAccessToken(code);
        try {
            accessToken = (AccessToken) JsonUtil.convertJsonBytesToObject(accessTokenJson, AccessToken.class);
            AthleteFTP athleteFTP = athleteRepositoryService.setAccessToken(accessToken, code);
            //            response.setCookie(cookie);
            mav.setViewName("redirect:/completeAthlete.html?username=" + athleteFTP.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
            mav.setViewName("redirect:/errorAccessToken.html");
        } catch (Exception e) {
            e.printStackTrace();
            mav.setViewName("redirect:/errorAccessToken.html");
        }
        return mav;
    }

}
