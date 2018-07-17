package com.jarics.trainbot.services;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.AthletesFeatures;
import com.jarics.trainbot.entities.SimpleSession;
import io.swagger.client.model.ActivityType;
import io.swagger.client.model.SummaryActivity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class GenerateTrainingDataset {
    public static void main(String[] args) throws IOException {
        //use java 8 lambda function
        NormalTrainingGenerator wNormalTrainingGenerator = new NormalTrainingGenerator();
        OverTrainingGenerator wOverTrainingGenerator = new OverTrainingGenerator();
        UndertrainingGenerator wUndertrainingGenerator = new UndertrainingGenerator();
        Files.write(Paths.get("trainingset_normal.csv"), AthletesFeatures.toHeaderString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        Files.write(Paths.get("trainingset_over.csv"), AthletesFeatures.toHeaderString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        Files.write(Paths.get("trainingset_under.csv"), AthletesFeatures.toHeaderString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        for (int i = 0; i < 100; i++) {
            AthleteFTP wNormalAthleteFTP = generateAthlete();
            AthleteFTP wUnderAthleteFTP = generateAthlete();
            AthleteFTP wOverAthleteFTP = generateAthlete();
            List<SimpleSession> wSimpleSessions = wNormalTrainingGenerator.getSession(wNormalAthleteFTP);
            write("normal", wSimpleSessions, wNormalAthleteFTP);
            wSimpleSessions = wOverTrainingGenerator.getSession(wOverAthleteFTP);
            write("over", wSimpleSessions, wOverAthleteFTP);
            wSimpleSessions = wUndertrainingGenerator.getSession(wUnderAthleteFTP);
            write("under", wSimpleSessions, wUnderAthleteFTP);
        }

    }

    private static void write(String pClass, List<SimpleSession> wSimpleSessions, AthleteFTP pAthleteFTP) {
        FeatureExtractor wFeatureExtractor = new FeatureExtractor();
        AthletesFeatures wAthletesFeatures =
                wFeatureExtractor.extract(
                        generateActivities(wSimpleSessions),
                        pAthleteFTP.getSwimFtp(),
                        pAthleteFTP.getBikeFtp(),
                        pAthleteFTP.getRunFtp());
        try {
            Files.write(Paths.get("trainingset_" + pClass + ".csv"), wAthletesFeatures.toCsvString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<SummaryActivity> generateActivities(List<SimpleSession> wSimpleSessions) {
        List<SummaryActivity> wSummaryActivities = new ArrayList<>();
        for (SimpleSession wSimpleSession : wSimpleSessions) {
            //swim
            SummaryActivity wActivity = new SummaryActivity();
            wActivity.setType(ActivityType.SWIM);
            wActivity.setDistance((float) wSimpleSession.getSwimDistance());
            wActivity.setMovingTime((int) Math.round(wSimpleSession.getTimeAtFtp()));
            wActivity.setElapsedTime((int) Math.round(wSimpleSession.getTimeAtFtp()));
            wSummaryActivities.add(wActivity);
            //bike
            wActivity = new SummaryActivity();
            wActivity.setType(ActivityType.RIDE);
            wActivity.setDistance((float) wSimpleSession.getBikeDistance());
            wActivity.setMovingTime((int) Math.round(wSimpleSession.getTimeAtFtp()));
            wActivity.setElapsedTime((int) Math.round(wSimpleSession.getTimeAtFtp()));
            wSummaryActivities.add(wActivity);
            //run
            wActivity = new SummaryActivity();
            wActivity.setType(ActivityType.RUN);
            wActivity.setDistance((float) wSimpleSession.getRunDistance());
            wActivity.setMovingTime((int) Math.round(wSimpleSession.getTimeAtFtp()));
            wActivity.setElapsedTime((int) Math.round(wSimpleSession.getTimeAtFtp()));
            wSummaryActivities.add(wActivity);
        }
        return wSummaryActivities;
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
