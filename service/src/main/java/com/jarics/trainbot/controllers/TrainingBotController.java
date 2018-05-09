package com.jarics.trainbot.controllers;


import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.SimpleSession;
import com.jarics.trainbot.services.TrainingBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        wSessions = trainingBotService.getSessions(pAthleteFTP);
        return wSessions;
    }

}
