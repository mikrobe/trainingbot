package com.jarics.trainbot.services;

import com.jarics.trainbot.com.jarics.trainbot.utils.FileUtils;
import com.jarics.trainbot.entities.AccessToken;
import com.jarics.trainbot.entities.AthleteFTP;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.WriteResult;
import org.dizitart.no2.objects.ObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

@Component
public class AthleteRepositoryService implements UserDetailsService {

    Nitrite db;
    ObjectRepository<AthleteFTP> repository;

    @Autowired
    public AthleteRepositoryService(@Value("${nitrite.dir}") final String nitriteDir,
                                    @Value("${nitrite.file.name}") final String nitriteFileName) {
        //java initialization
        try {
            FileUtils.prepareDir(nitriteDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String aPath = nitriteDir + nitriteFileName;
        if (System.getProperty("testing") != null) {
            aPath = aPath + "_" + UUID.randomUUID();
        }
        db = Nitrite.builder()
                .compressed()
                .filePath(aPath)
                .openOrCreate("system", "admin");
        repository = db.getRepository(AthleteFTP.class);
    }

    /**
     * This method creates an athlete. Will throw error if athlete with same username exists.
     * @param pAthleteFTP
     * @return
     * @throws Exception
     */
    public AthleteFTP createAthleteFTP(AthleteFTP pAthleteFTP) throws Exception {
        AthleteFTP wAthleteFTP;
        wAthleteFTP = findAthleteFtpByUsername(pAthleteFTP.getUsername());
        if (wAthleteFTP != null) throw new Exception("Athlete with same username already exists...");
        pAthleteFTP.setId(NitriteId.newId().getIdValue());
        WriteResult result = repository.insert(pAthleteFTP);
        return getAthleteFromResult(wAthleteFTP, result);
    }

    /**
     * Make sure you get athlete before updating.
     */
    public AthleteFTP updateAthleteFTP(AthleteFTP pAthleteFTP) throws Exception {
        AthleteFTP wAthleteFTP = null;
        // make sure same id with same username
        wAthleteFTP = getAthleteById(pAthleteFTP.getId());
        if (!wAthleteFTP.getUsername().equals(pAthleteFTP.getUsername()))
            throw new Exception("Illegal update");
        WriteResult writeResult = repository.update(pAthleteFTP);
        return getAthleteFromResult(wAthleteFTP, writeResult);
    }

    private AthleteFTP getAthleteFromResult(AthleteFTP wAthleteFTP, WriteResult writeResult) {
        for (NitriteId id : writeResult) {
            wAthleteFTP = repository.getById(id);
        }
        return wAthleteFTP;
    }

    public AthleteFTP findAthleteFtpByUsername(String pUsername) {

        AthleteFTP wAthleteFTP = null;
        org.dizitart.no2.objects.Cursor<AthleteFTP> wCursor = repository.find(eq("username", pUsername));
        for (AthleteFTP wFtp : wCursor) {
            wAthleteFTP = wFtp;
        }
        return wAthleteFTP;
    }

    public AthleteFTP removeAthlete(String pUsername) {
        AthleteFTP wAthleteFTP = findAthleteFtpByUsername(pUsername);
        WriteResult writeResult = repository.remove(wAthleteFTP);
        return wAthleteFTP;
    }

    public AthleteFTP getAthleteById(long athleteFtpId) {
        AthleteFTP wAthleteFTP = new AthleteFTP();
        org.dizitart.no2.objects.Cursor<AthleteFTP> wCursor = repository.find(eq("id", athleteFtpId));
        for (AthleteFTP wFtp : wCursor) {
            wAthleteFTP = wFtp;
        }
        return wAthleteFTP;
    }


    public AthleteFTP setAccessToken(AccessToken accessToken, String code) throws Exception {
        AthleteFTP athleteFTP = null;
        //get athlete
        athleteFTP = findAthleteFtpByUsername(accessToken.getAthlete().getUsername());
        if (athleteFTP == null) {
            athleteFTP = new AthleteFTP();
            athleteFTP.setUsername(accessToken.getAthlete().getUsername());
            createAthleteFTP(athleteFTP);
        }
        athleteFTP.setTokenType(accessToken.getTokenType());
        athleteFTP.setAccessToken(accessToken.getAccessToken());
        athleteFTP.setCode(code);
        return updateAthleteFTP(athleteFTP);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AthleteFTP athleteFTP = this.findAthleteFtpByUsername(s);
        if (athleteFTP == null) throw new UsernameNotFoundException("User not found");
        return athleteFTP;
    }
}
