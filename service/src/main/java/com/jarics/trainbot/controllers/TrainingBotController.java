package com.jarics.trainbot.controllers;


import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.AthletesFeatures;
import com.jarics.trainbot.entities.SimpleSession;
import com.jarics.trainbot.services.AthleteRepositoryService;
import com.jarics.trainbot.services.MLClasses;
import com.jarics.trainbot.services.learning.WekaMLService;
import com.jarics.trainbot.services.sessions.TrainingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8081")
public class TrainingBotController {

    AthleteRepositoryService athleteRepositoryService;
    TrainingPlanService trainingPlanService;
    WekaMLService wekaMLService;

    @Autowired
    public TrainingBotController(
            AthleteRepositoryService athleteRepositoryService,
            TrainingPlanService trainingPlanService, WekaMLService wekaMLService) {
        this.athleteRepositoryService = athleteRepositoryService;
        this.trainingPlanService = trainingPlanService;
        this.wekaMLService = wekaMLService;
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
    public MLClasses createAthlete(@RequestBody AthletesFeatures athletesFeatures) throws Exception {
        return wekaMLService.classify(athletesFeatures.gettSB(), athletesFeatures.getcTL(), athletesFeatures.getaTL());
    }


}
