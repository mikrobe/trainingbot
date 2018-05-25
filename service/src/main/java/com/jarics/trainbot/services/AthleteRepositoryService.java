package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.AthleteFTP;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.WriteResult;
import org.dizitart.no2.objects.ObjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

@Component
public class AthleteRepositoryService {

    Nitrite db;
    ObjectRepository<AthleteFTP> repository;
    @Value("${nitrite-db-file-path}")
    private String nitriteDbPath;

    public AthleteRepositoryService() {
        //java initialization
        db = Nitrite.builder()
                .compressed()
                .filePath(nitriteDbPath)
                .openOrCreate("system", "admin");
        repository = db.getRepository(AthleteFTP.class);
    }

    public AthleteFTP setAthleteFTP(AthleteFTP pAthleteFTP) {
        if (pAthleteFTP.getId() > 0)
            return updateAthleteFTP(pAthleteFTP);
        AthleteFTP wAthleteFTP = new AthleteFTP();
        pAthleteFTP.setId(NitriteId.newId().getIdValue());
        WriteResult result = repository.insert(pAthleteFTP);
        return getAthleteFromResult(wAthleteFTP, result);
    }

    public AthleteFTP updateAthleteFTP(AthleteFTP pAthleteFTP) {
        AthleteFTP wAthleteFTP = new AthleteFTP();
        WriteResult writeResult = repository.update(pAthleteFTP);
        return getAthleteFromResult(wAthleteFTP, writeResult);
    }

    private AthleteFTP getAthleteFromResult(AthleteFTP wAthleteFTP, WriteResult writeResult) {
        for (NitriteId id : writeResult) {
            wAthleteFTP = repository.getById(id);
        }
        return wAthleteFTP;
    }

    public AthleteFTP getAthleteFtp(long athleteFtpId) {
        AthleteFTP wAthleteFTP = new AthleteFTP();
        org.dizitart.no2.objects.Cursor<AthleteFTP> wCursor = repository.find(eq("id", athleteFtpId));
        for (AthleteFTP wFtp : wCursor) {
            wAthleteFTP = wFtp;
        }
        return wAthleteFTP;
    }
}
