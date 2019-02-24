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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;


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
  public SimpleSession session(@PathVariable("username") String pUsername,
      @RequestParam(value = "week") int week) {
//        return trainingBotService.getSession(pUsername, week);
    //TODO not sure
    return null;
  }

  @RequestMapping(value = "/athlete", method = RequestMethod.POST, produces = "application/json")
  public AthleteFTP createAthlete(@RequestBody AthleteFTP pAthleteFTP) throws Exception {
    return athleteRepositoryService.createAthleteFTP(pAthleteFTP);
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
  public AthleteFTP classifyAthlete(@RequestBody AthletesFeatures athletesFeatures)
      throws Exception {

    MLClasses clazz = wekaMLService
        .classify(athletesFeatures.gettSB(), athletesFeatures.getcTL(), athletesFeatures.getaTL());
    AthleteFTP athleteFTP;
    athleteFTP = athleteRepositoryService
        .findAthleteFtpByUsername(athletesFeatures.getAthlete().getUsername());
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
    String accessTokenJson = stravaService.getAccessToken(code);
    try {
      accessToken = (AccessToken) JsonUtil
          .convertJsonBytesToObject(accessTokenJson, AccessToken.class);
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
