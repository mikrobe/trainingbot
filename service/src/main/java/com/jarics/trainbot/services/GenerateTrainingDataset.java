package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.*;
import org.apache.commons.io.FileDeleteStrategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class GenerateTrainingDataset {
    static String wGenDir = "trainingDataSets/";
    static String wGenRawDir = "trainingRawData/";
    public static void main(String[] args) throws IOException {
        //use java 8 lambda function
        TrainingBotService wNormalTrainingGenerator = new TrainingBotService();
        OverTrainingGenerator wOverTrainingGenerator = new OverTrainingGenerator();
        UndertrainingGenerator wUndertrainingGenerator = new UndertrainingGenerator();
        prepareDir(wGenDir);
        prepareDir(wGenRawDir);
        //features headers
        Files.write(Paths.get(wGenDir + MLClasses.normal + ".csv"), AthletesFeatures.toHeaderString().getBytes(), StandardOpenOption.CREATE);
        Files.write(Paths.get(wGenDir + MLClasses.undertrained + ".csv"), AthletesFeatures.toHeaderString().getBytes(), StandardOpenOption.CREATE);
        Files.write(Paths.get(wGenDir + MLClasses.overtrained + ".csv"), AthletesFeatures.toHeaderString().getBytes(), StandardOpenOption.CREATE);

        for (int i = 0; i < 100; i++) {
            AthleteFTP wNormalAthleteFTP = generateAthlete();
            wNormalAthleteFTP.setClassification(MLClasses.normal);
            AthleteFTP wUnderAthleteFTP = generateAthlete();
            wUnderAthleteFTP.setClassification(MLClasses.undertrained);
            AthleteFTP wOverAthleteFTP = generateAthlete();
            wOverAthleteFTP.setClassification(MLClasses.overtrained);

            List<SimpleSession> wSimpleSessions = wNormalTrainingGenerator.getSession(wNormalAthleteFTP);
            write(wNormalAthleteFTP, wSimpleSessions);
            write(wSimpleSessions, wNormalAthleteFTP);

            wSimpleSessions = wOverTrainingGenerator.getSession(wOverAthleteFTP);
            write(wOverAthleteFTP, wSimpleSessions);
            write(wSimpleSessions, wOverAthleteFTP);

            wSimpleSessions = wUndertrainingGenerator.getSession(wUnderAthleteFTP);
            write(wUnderAthleteFTP, wSimpleSessions);
            write(wSimpleSessions, wUnderAthleteFTP);
        }
    }

    static private void prepareDir(String wDir) throws IOException {
        if (!Files.exists(Paths.get(wDir))) {
            Files.createDirectory(Paths.get(wDir));
        } else {
            File fin = Paths.get(wDir).toFile();
            for (File file : fin.listFiles()) {
                FileDeleteStrategy.FORCE.delete(file);
            }
        }
    }

    private static void write(AthleteFTP wNormalAthleteFTP, List<SimpleSession> wSimpleSessions) {
        try {
            Files.write(Paths.get(wGenRawDir + wNormalAthleteFTP.getUsername() + "_" + wNormalAthleteFTP.getClassification() + ".csv"), SimpleSession.toHeaderString().getBytes());
            for (SimpleSession wSimpleSession : wSimpleSessions) {
                Files.write(Paths.get(wGenRawDir + wNormalAthleteFTP.getUsername() + "_" + wNormalAthleteFTP.getClassification() + ".csv"), wSimpleSession.toCsvString().getBytes(), StandardOpenOption.APPEND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void write(List<SimpleSession> wSimpleSessions, AthleteFTP pAthleteFTP) {
        FeatureExtractor wFeatureExtractor = new FeatureExtractor();
        AthletesFeatures wAthletesFeatures =
                wFeatureExtractor.extract(
                        generateActivities(wSimpleSessions),
                        pAthleteFTP.getSwimFtp(),
                        pAthleteFTP.getBikeFtp(),
                        pAthleteFTP.getRunFtp());
        wAthletesFeatures.setAthlete(pAthleteFTP.getUsername());
        try {
            Files.write(Paths.get(wGenDir + pAthleteFTP.getClassification() + ".csv"), wAthletesFeatures.toCsvString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<AthleteActivity> generateActivities(List<SimpleSession> wSimpleSessions) {
        List<AthleteActivity> wAthleteActivities = new ArrayList<>();
        for (SimpleSession wSimpleSession : wSimpleSessions) {
            //swim
            AthleteActivity wActivity = new AthleteActivity();
            wActivity.setType(BotActivityType.SWIM);
            wActivity.setDistance((float) wSimpleSession.getSwimDistance());
            wActivity.setMovingTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
            wActivity.setElapsedTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
            wAthleteActivities.add(wActivity);
            //bike
            wActivity = new AthleteActivity();
            wActivity.setType(BotActivityType.BIKE);
            wActivity.setDistance((float) wSimpleSession.getBikeDistance());
            wActivity.setMovingTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
            wActivity.setElapsedTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
            wActivity.setWeigthedAvgWatts(wSimpleSession.getBikeFtp());
            wAthleteActivities.add(wActivity);
            //run
            wActivity = new AthleteActivity();
            wActivity.setType(BotActivityType.RUN);
            wActivity.setDistance((float) wSimpleSession.getRunDistance());
            wActivity.setMovingTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
            wActivity.setElapsedTime((int) Math.round(wSimpleSession.getTimeAtFtp()) * 60);
            wAthleteActivities.add(wActivity);
        }
        return wAthleteActivities;
    }

    public static AthleteFTP generateAthlete() {
        double wRunFtp = new Random().doubles(275, 450).findFirst().getAsDouble();
        double wSwimFtp = new Random().doubles(80, 120).findFirst().getAsDouble();
        double wBikeFtp = new Random().doubles(180, 320).findFirst().getAsDouble();

        AthleteFTP wAthleteFTP = new AthleteFTP();
        wAthleteFTP.setRunFtp(wRunFtp); //5:45 --> 300+45 = 345
        wAthleteFTP.setBikeFtp(wBikeFtp);
        wAthleteFTP.setSwimFtp(wSwimFtp);
        wAthleteFTP.setTarget(2);
        wAthleteFTP.setUsername(UUID.randomUUID().toString());
        return wAthleteFTP;
    }
}
