package com.jarics.trainbot.security;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.services.AthleteRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SecurityInit implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired private AthleteRepositoryService athleteRepositoryService;

    private String adminUser = "admin";
    private String adminPassword = "Triat001!";

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        AthleteFTP wAthleteFTP = new AthleteFTP();
        wAthleteFTP.setPassword(adminPassword);
        wAthleteFTP.setUsername(adminUser);
        //TODO         wAthleteFTP.addAuthorities("admin");
        try {
            AthleteFTP athleteFTP = athleteRepositoryService.createAthleteFTP(wAthleteFTP);
        } catch (Exception e) {
            if (!e
              .getMessage()
              .contains("Athlete with same username already exists")) {
                e.printStackTrace();
            }
        }
    }
}



