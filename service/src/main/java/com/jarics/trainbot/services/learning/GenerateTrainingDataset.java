package com.jarics.trainbot.services.learning;

import com.jarics.trainbot.com.jarics.trainbot.utils.FileUtils;
import com.jarics.trainbot.entities.*;
import com.jarics.trainbot.services.EventTypes;
import com.jarics.trainbot.services.MLClasses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class GenerateTrainingDataset {


    @Value("${training.dataset.dir}")
    private String trainingDataSetDir;

    @Value("${training.dataset.filename}")
    private String trainingDataSetFileName;

    @Value("${raw.dataset.dir}")
    private String rawDataSetDir;

    @Autowired
    NormalTrainingGenerator wNormalTrainingGenerator;

    @Autowired
    OverTrainingGenerator wOverTrainingGenerator;

    @Autowired
    UndertrainingGenerator wUndertrainingGenerator;


    public void generate(boolean keepRawData) throws Exception {
        FileUtils.prepareDir(trainingDataSetDir);
        FileUtils.prepareDir(rawDataSetDir);
        LocalDateTime currentTime = LocalDateTime.now();
        Files.write(Paths.get(trainingDataSetDir + trainingDataSetFileName), AthletesFeatures.toArffHeader().getBytes(), StandardOpenOption.CREATE);
        for (int i = 0; i < 100; i++) {
            AthleteFTP wNormalAthleteFTP = generateAthlete();
            wNormalAthleteFTP.setClassification(MLClasses.normal);
            AthleteFTP wUnderAthleteFTP = generateAthlete();
            wUnderAthleteFTP.setClassification(MLClasses.undertrained);
            AthleteFTP wOverAthleteFTP = generateAthlete();
            wOverAthleteFTP.setClassification(MLClasses.overtrained);

            List<SimpleSession> wSimpleSessions = wNormalTrainingGenerator.getSessions(wNormalAthleteFTP, 20);
            List<AthleteActivity> wActivities = generateActivities(wSimpleSessions);
            if (keepRawData) writeRawData(wNormalAthleteFTP, wActivities);
            writeFeatures(wNormalAthleteFTP, wActivities);

            wSimpleSessions = wOverTrainingGenerator.getSessions(wOverAthleteFTP, 20);
            wActivities = generateActivities(wSimpleSessions);
            if (keepRawData) writeRawData(wOverAthleteFTP, wActivities);
            writeFeatures(wOverAthleteFTP, wActivities);

            wSimpleSessions = wUndertrainingGenerator.getSessions(wUnderAthleteFTP, 20);
            wActivities = generateActivities(wSimpleSessions);
            if (keepRawData) writeRawData(wUnderAthleteFTP, wActivities);
            writeFeatures(wUnderAthleteFTP, wActivities);
        }
    }



    public void writeRawData(AthleteFTP wNormalAthleteFTP, List<AthleteActivity> wActivities) {
        try {
            Files.write(Paths.get(rawDataSetDir + wNormalAthleteFTP.getUsername() + "_" + wNormalAthleteFTP.getClassification() + ".csv"), AthleteActivity.toHeaderString().getBytes());
            for (AthleteActivity wAthleteActivity : wActivities) {
                Files.write(Paths.get(rawDataSetDir + wNormalAthleteFTP.getUsername() + "_" + wNormalAthleteFTP.getClassification() + ".csv"), wAthleteActivity.toCsvString().getBytes(), StandardOpenOption.APPEND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeFeatures(AthleteFTP pAthleteFTP, List<AthleteActivity> wAthleteActivities) {
        FeatureExtractor wFeatureExtractor = new FeatureExtractor();
        AthletesFeatures wAthletesFeatures =
                wFeatureExtractor.extract(
                        wAthleteActivities,
                        pAthleteFTP.getSwimFtp(),
                        pAthleteFTP.getBikeFtp(),
                        pAthleteFTP.getRunFtp());
        wAthletesFeatures.setAthlete(pAthleteFTP);
        try {
            Files.write(Paths.get(trainingDataSetDir + trainingDataSetFileName), wAthletesFeatures.toArffData().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<AthleteActivity> generateActivities(List<SimpleSession> wSimpleSessions) {
        List<AthleteActivity> wAthleteActivities = new ArrayList<>();
        for (SimpleSession wSimpleSession : wSimpleSessions) {
            wAthleteActivities.add(newSwimDistanceActivity(wSimpleSession));
            wAthleteActivities.add(newSwimIntensityActivity(wSimpleSession));
            wAthleteActivities.add(newBikeActivity(wSimpleSession));
            wAthleteActivities.add(newRunActivity(wSimpleSession));
        }
        return wAthleteActivities;
    }

    private AthleteActivity newBikeActivity(SimpleSession wSimpleSession) {
        AthleteActivity wActivity;
        wActivity = new AthleteActivity();
        wActivity.setWeekNbr(wSimpleSession.getWeek());
        wActivity.setType(BotActivityType.BIKE);
        wActivity.setDistance((float) wSimpleSession.getBikeDistance());
        wActivity.setMovingTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
        wActivity.setElapsedTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
        wActivity.setWeigthedAvgWatts((wSimpleSession.getBikeHZone() + wSimpleSession.getBikeHZone()) / 2);
        return wActivity;
    }

    private AthleteActivity newRunActivity(SimpleSession wSimpleSession) {
        AthleteActivity wActivity;
        wActivity = new AthleteActivity();
        wActivity.setWeekNbr(wSimpleSession.getWeek());
        wActivity.setType(BotActivityType.RUN);
        wActivity.setDistance((float) wSimpleSession.getRunDistance());
        wActivity.setMovingTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
        wActivity.setElapsedTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
        wActivity.setPace((wSimpleSession.getRunHZone() + wSimpleSession.getRunHZone()) / 2);
        return wActivity;
    }

    //TODO Zone 2 5 secs slower than T-Time
    private AthleteActivity newSwimDistanceActivity(SimpleSession pSimpleSession) {
        AthleteActivity wActivity = new AthleteActivity();
        wActivity.setWeekNbr(pSimpleSession.getWeek());
        wActivity.setType(BotActivityType.SWIM);
        wActivity.setDistance((float) pSimpleSession.getSwimDistance());
        wActivity.setMovingTime((int) Math.round(pSimpleSession.getTimeAtFtp()) * 60);
        wActivity.setElapsedTime((int) Math.round(pSimpleSession.getTimeAtFtp()) * 60);
//        wActivity.setPace(  );
        return wActivity;
    }

    private AthleteActivity newSwimIntensityActivity(SimpleSession pSimpleSession) {
        AthleteActivity wActivity = new AthleteActivity();
//        wActivity.setWeekNbr(SimpleSession pSimpleSession.getWeek());
        wActivity.setType(BotActivityType.SWIM);
        double avgPace = (pSimpleSession.getSwimHZone() + pSimpleSession.getSwimLZone()) / 2;
        wActivity.setDistance((float) (pSimpleSession.getTimeAtFtp() * 100 / avgPace));
        wActivity.setMovingTime((int) Math.round(pSimpleSession.getTimeAtFtp()) * 60);
        wActivity.setElapsedTime((int) Math.round(pSimpleSession.getTimeAtFtp()) * 60);
        wActivity.setPace(avgPace);
        return wActivity;
    }
//
//    private AthleteActivity newRunDistanceActivity(){
//        AthleteActivity wActivity = new AthleteActivity();
//        wActivity.setWeekNbr(wSimpleSession.getWeek());
//        wActivity.setType(BotActivityType.RUN);
//        wActivity.setDistance((float) wSimpleSession.getRunDistance());
//        wActivity.setMovingTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
//        wActivity.setElapsedTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
//        wActivity.setPace( (wSimpleSession.getTimeAtFtp()/wSimpleSession.getRunDistance()) * 60);
//    }
//
//    private AthleteActivity newRunIntensityActivity(){
//        AthleteActivity wActivity = new AthleteActivity();
//        wActivity.setWeekNbr(wSimpleSession.getWeek());
//        wActivity.setType(BotActivityType.RUN);
//        wActivity.setDistance((float) wSimpleSession.getRunDistance());
//        wActivity.setMovingTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
//        wActivity.setElapsedTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
//        wActivity.setPace( (wSimpleSession.getTimeAtFtp()/wSimpleSession.getRunDistance()) * 60);
//    }

    public AthleteFTP generateAthlete() {
        double wRunFtp = 332;
        double wSwimFtp = 101;
        double wBikeFtp = 228;
        AthleteFTP wAthleteFTP = new AthleteFTP();
        wAthleteFTP.setRunFtp(wRunFtp); //5:45 --> 300+45 = 345
        wAthleteFTP.setBikeFtp(wBikeFtp);
        wAthleteFTP.setSwimFtp(wSwimFtp);
        wAthleteFTP.setEventType(EventTypes.olympic);
        wAthleteFTP.setUsername(UUID.randomUUID().toString());
        return wAthleteFTP;
    }
}
