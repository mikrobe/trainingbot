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
public class TrainingBotController {

    @Autowired
    TrainingBotService trainingBotService;

    @Autowired
    AthleteRepositoryService athleteRepositoryService;


    @RequestMapping(value = "/athlete/plan", method = RequestMethod.POST, produces = "application/json")
    public List<SimpleSession> create(@RequestBody AthleteFTP pAthleteFTP) {
        List<SimpleSession> wSessions = null;
        wSessions = trainingBotService.getSession(pAthleteFTP);
        return wSessions;
    }

    @RequestMapping(value = "/athlete/session", method = RequestMethod.POST, produces = "application/json")
    public SimpleSession create(@RequestBody AthleteFTP pAthleteFTP, @RequestParam(value = "week") int week) {
        return trainingBotService.getSession(pAthleteFTP, week);
    }

    @RequestMapping(value = "/athlete", method = RequestMethod.POST, produces = "application/json")
    public AthleteFTP createAthlete(@RequestBody AthleteFTP pAthleteFTP) {
        return athleteRepositoryService.setAthleteFTP(pAthleteFTP);
    }

    @RequestMapping(value = "/athlete", method = RequestMethod.GET, produces = "application/json")
    public AthleteFTP getAthlete(@RequestParam(value = "id") long athleteFtpId) {
        return athleteRepositoryService.getAthleteFtp(athleteFtpId);
    }

}
