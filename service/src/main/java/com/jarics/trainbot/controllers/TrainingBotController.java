package com.jarics.trainbot.controllers;


import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.SimpleSession;
import com.jarics.trainbot.services.TrainingBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class TrainingBotController {

    final
    TrainingBotService trainingBotService;

    @Autowired
    public TrainingBotController(TrainingBotService trainingBotService) {
        this.trainingBotService = trainingBotService;
    }

    @RequestMapping(value = "/plan", method = RequestMethod.POST, produces = "application/json")
    public List<SimpleSession> create(@RequestBody AthleteFTP pAthleteFTP) {
        List<SimpleSession> wSessions = null;
        wSessions = trainingBotService.getSession(pAthleteFTP);
        return wSessions;
    }

    @RequestMapping(value = "/session", method = RequestMethod.POST, produces = "application/json")
    public SimpleSession create(@RequestBody AthleteFTP pAthleteFTP, @RequestParam(value = "week") int week) {
        return trainingBotService.getSession(pAthleteFTP, week);
    }

}
