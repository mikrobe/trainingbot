package com.jarics.trainbot.services.learning;

import com.jarics.trainbot.com.jarics.trainbot.utils.FileUtils;
import com.jarics.trainbot.entities.AthleteActivity;
import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.AthletesFeatures;
import com.jarics.trainbot.plan.EventType;
import com.jarics.trainbot.plan.Plan;
import com.jarics.trainbot.plan.PlannedWeek;
import com.jarics.trainbot.plan.Session;
import com.jarics.trainbot.services.MLClasses;
import com.jarics.trainbot.services.PlanService;
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


    public void generate(boolean keepRawData) throws Exception {
        ActivitiesGenerator activitiesGenerator = new ActivitiesGenerator();
        FileUtils.prepareDir(trainingDataSetDir);
        FileUtils.prepareDir(rawDataSetDir);
        LocalDateTime currentTime = LocalDateTime.now();
        Files.write(Paths.get(trainingDataSetDir + trainingDataSetFileName), AthletesFeatures.toArffHeader().getBytes(), StandardOpenOption.CREATE);
        for (int i = 0; i < 100; i++) {

            PlanService planService = new PlanService();

            AthleteFTP wAthleteFTP = generateAthlete();
            wAthleteFTP.setClassification(MLClasses.overtrained);
            Plan plan = planService.getTriathlonOverTrainingPlan(wAthleteFTP);
            generateActivities(plan, keepRawData, activitiesGenerator, wAthleteFTP);

            wAthleteFTP = generateAthlete();
            wAthleteFTP.setClassification(MLClasses.undertrained);
            plan = planService.getTriathlonUnderTrainingPlan(wAthleteFTP);
            generateActivities(plan, keepRawData, activitiesGenerator, wAthleteFTP);

            wAthleteFTP = generateAthlete();
            wAthleteFTP.setClassification(MLClasses.normal);
            plan = planService.getTriathlonPlan(wAthleteFTP);
            generateActivities(plan, keepRawData, activitiesGenerator, wAthleteFTP);

        }
    }

    private void generateActivities(Plan plan, boolean keepRawData, ActivitiesGenerator activitiesGenerator, AthleteFTP wOverAthleteFTP) {
        List<AthleteActivity> wActivities = new ArrayList<>();
        for (PlannedWeek plannedWeek : plan.getPlannedWeeks()) {
            List<Session> sessions = new ArrayList<>();
            sessions.add(plannedWeek.getSwimVolumeSession());
            sessions.add(plannedWeek.getSwimIntevalSession());
            sessions.add(plannedWeek.getBikeVolumeSession());
            sessions.add(plannedWeek.getBikeIntevalSession());
            sessions.add(plannedWeek.getRunVolumeSession());
            sessions.add(plannedWeek.getRunIntevalSession());
            wActivities.addAll(activitiesGenerator.generateActivities(plannedWeek.getWeek(), sessions));
        }
        if (keepRawData) writeRawData(wOverAthleteFTP, wActivities);
        writeFeatures(wOverAthleteFTP, wActivities);
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
        wAthleteFTP.setEventType(EventType.olympic);
        wAthleteFTP.setUsername(UUID.randomUUID().toString());
        return wAthleteFTP;
    }
}
