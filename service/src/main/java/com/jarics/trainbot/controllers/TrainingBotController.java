package com.jarics.trainbot.controllers;


import com.jarics.trainbot.entities.Session;
import com.jarics.trainbot.entities.TrainingInfo;
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

    @Autowired
    TrainingBotService trainingBotService;

    @RequestMapping(value = "/plan", method = RequestMethod.POST, produces = "application/json")
    public List<Session> create(@RequestBody TrainingInfo pTrainingInfo) {
        List<Session> wSessions = trainingBotService.getSessions(
                pTrainingInfo.getCurrentWeek(),
                pTrainingInfo.getFtp(),
                pTrainingInfo.getNumberOfWeeks());
        return wSessions;
    }


}
