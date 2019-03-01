package com.jarics.trainbot.services.learning;

import com.jarics.trainbot.com.jarics.trainbot.utils.FileUtils;
import com.jarics.trainbot.entities.AthleteActivity;
import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.AthletesFeatures;
import com.jarics.trainbot.entities.SimpleSession;
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
        ActivitiesGenerator activitiesGenerator = new ActivitiesGenerator();
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
            List<AthleteActivity> wActivities = activitiesGenerator.generateActivities(wSimpleSessions);
            if (keepRawData) writeRawData(wNormalAthleteFTP, wActivities);
            writeFeatures(wNormalAthleteFTP, wActivities);

            wSimpleSessions = wOverTrainingGenerator.getSessions(wOverAthleteFTP, 20);
            wActivities = activitiesGenerator.generateActivities(wSimpleSessions);
            if (keepRawData) writeRawData(wOverAthleteFTP, wActivities);
            writeFeatures(wOverAthleteFTP, wActivities);

            wSimpleSessions = wUndertrainingGenerator.getSessions(wUnderAthleteFTP, 20);
            wActivities = activitiesGenerator.generateActivities(wSimpleSessions);
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
