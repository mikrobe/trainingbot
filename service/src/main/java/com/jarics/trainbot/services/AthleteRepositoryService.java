package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.AthleteFTP;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.objects.ObjectRepository;
import org.springframework.stereotype.Component;

@Component
public class AthleteRepositoryService {

    Nitrite db;
    ObjectRepository<AthleteFTP> repository;

    public AthleteRepositoryService() {
        //java initialization
        db = Nitrite.builder()
                .compressed()
                .filePath("/Users/erickaudet/Documents/trainingBot.db")
                .openOrCreate("system", "admin");

        // Create a Nitrite Collection
//        NitriteCollection collection = db.getCollection("trainingBot");

        repository = db.getRepository(AthleteFTP.class);
    }

    public AthleteFTP getAthleteFTP(AthleteFTP pAthleteFTP) {
        long wNewId = pAthleteFTP.getId();
        if (pAthleteFTP.getId() == 0) {
            NitriteId wNitriteId = NitriteId.newId();
            wNewId = wNitriteId.getIdValue();
            pAthleteFTP.setId(wNewId);
            repository.insert(pAthleteFTP);
        }
        return pAthleteFTP;
    }
}
