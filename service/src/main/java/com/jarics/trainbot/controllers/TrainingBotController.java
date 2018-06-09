package com.jarics.trainbot.controllers;


import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.SimpleSession;
import com.jarics.trainbot.services.AthleteRepositoryService;
import com.jarics.trainbot.services.TrainingBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8081")
public class TrainingBotController {

    @Autowired
    TrainingBotService trainingBotService;

    @Autowired
    AthleteRepositoryService athleteRepositoryService;


    @RequestMapping(value = "/athlete/plan/{username}", method = RequestMethod.GET, produces = "application/json")
    public List<SimpleSession> plan(@PathVariable("username") String pUsername) {
        List<SimpleSession> wSessions = null;
        wSessions = trainingBotService.getSession(pUsername);
        return wSessions;
    }

    @RequestMapping(value = "/athlete/session/{username}", method = RequestMethod.GET, produces = "application/json")
    public SimpleSession session(@PathVariable("username") String pUsername, @RequestParam(value = "week") int week) {
        return trainingBotService.getSession(pUsername, week);
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

}
