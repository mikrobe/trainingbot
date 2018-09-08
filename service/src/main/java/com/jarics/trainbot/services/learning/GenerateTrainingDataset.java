package com.jarics.trainbot.services.learning;

import com.jarics.trainbot.entities.*;
import com.jarics.trainbot.services.EventTypes;
import com.jarics.trainbot.services.MLClasses;
import org.apache.commons.io.FileDeleteStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
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


    @Value("${training.data.set.dir}")
    private String trainingDataSetDir;

    @Value("${raw.data.set.dir}")
    private String rawDataSetDir;

    @Autowired
    NormalTrainingGenerator wNormalTrainingGenerator;

    @Autowired
    OverTrainingGenerator wOverTrainingGenerator;

    @Autowired
    UndertrainingGenerator wUndertrainingGenerator;

    String rawDataFileName;

    String featuresFileName;

    public void generate(boolean keepRawData) throws Exception {
        prepareDir(trainingDataSetDir);
        prepareDir(rawDataSetDir);
        LocalDateTime currentTime = LocalDateTime.now();
        featuresFileName = "activity_features.arff";
        Files.write(Paths.get(trainingDataSetDir + featuresFileName), AthletesFeatures.toArffHeader().getBytes(), StandardOpenOption.CREATE_NEW);
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

    private void prepareDir(String wDir) throws IOException {
        if (!Files.exists(Paths.get(wDir))) {
            Files.createDirectory(Paths.get(wDir));
        } else {
            File fin = Paths.get(wDir).toFile();
            for (File file : fin.listFiles()) {
                FileDeleteStrategy.FORCE.delete(file);
            }
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
            Files.write(Paths.get(trainingDataSetDir + featuresFileName), wAthletesFeatures.toArffData().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<AthleteActivity> generateActivities(List<SimpleSession> wSimpleSessions) {
        List<AthleteActivity> wAthleteActivities = new ArrayList<>();
        for (SimpleSession wSimpleSession : wSimpleSessions) {
            wAthleteActivities.add(newSwimDistanceActivity(wSimpleSession));
//            wAthleteActivities.add(newSwimIntensityActivity(wSimpleSession));
            wAthleteActivities.add(newBikeDistanceActivity(wSimpleSession));
            wAthleteActivities.add(newBikeIntensityActivity(wSimpleSession));
//            wAthleteActivities.add(newRunDistanceActivity(wSimpleSession));
//            wAthleteActivities.add(newRunIntensityActivity(wSimpleSession));
        }
        return wAthleteActivities;
    }

    private AthleteActivity newBikeDistanceActivity(SimpleSession wSimpleSession) {
        AthleteActivity wActivity;
        wActivity = new AthleteActivity();
        wActivity.setWeekNbr(wSimpleSession.getWeek());
        wActivity.setType(BotActivityType.BIKE);
        wActivity.setDistance((float) wSimpleSession.getBikeDistance());
        wActivity.setMovingTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
        wActivity.setElapsedTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
        return wActivity;
    }

    private AthleteActivity newBikeIntensityActivity(SimpleSession wSimpleSession) {
        AthleteActivity wActivity;
        wActivity = new AthleteActivity();
        wActivity.setWeekNbr(wSimpleSession.getWeek());
        wActivity.setType(BotActivityType.BIKE);
        wActivity.setDistance((float) wSimpleSession.getBikeDistance());
        wActivity.setMovingTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
        wActivity.setElapsedTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
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
        wActivity.setPace((pSimpleSession.getTimeAtFtp() * 100 / pSimpleSession.getSwimDistance()) * 60);
        return wActivity;
    }

//    private AthleteActivity newSwimIntensityActivity(){
//        AthleteActivity wActivity = new AthleteActivity();
//        wActivity.setWeekNbr(wSimpleSession.getWeek());
//        wActivity.setType(BotActivityType.SWIM);
//        wActivity.setDistance((float) wSimpleSession.getSwimDistance());
//        wActivity.setMovingTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
//        wActivity.setElapsedTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
//        wActivity.setPace( (wSimpleSession.getTimeAtFtp()*100/wSimpleSession.getSwimDistance()) * 60);
//    }
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