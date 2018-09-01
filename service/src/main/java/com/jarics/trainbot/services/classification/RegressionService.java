package com.jarics.trainbot.services.classification;

import com.jarics.trainbot.entities.AthleteFTP;
import com.jarics.trainbot.entities.AthletesFeatures;
import com.jarics.trainbot.services.MLClasses;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.apache.spark.mllib.util.MLUtils;
import org.springframework.stereotype.Component;
import scala.Tuple2;

import java.util.HashMap;
import java.util.Map;

@Component
/**
 * This classifier will return MLClass based on regression.
 */
public class RegressionService implements MLServiceIf {
    @Override
    public MLClasses classify(AthleteFTP pAthleteFTP, AthletesFeatures pAthletesFeatures) {
        System.setProperty("hadoop.home.dir", "/");
        SparkConf sparkConf = new SparkConf().setAppName("RegressionService");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
// Load and parse the data file.
        String datapath = "/Users/erickaudet/dev/trainingbot/test_data/sample_libsvm_data.txt";
        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(jsc.sc(), datapath).toJavaRDD();
// Split the data into training and test sets (30% held out for testing)
        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.7, 0.3});
        JavaRDD<LabeledPoint> trainingData = splits[0];
        JavaRDD<LabeledPoint> testData = splits[1];

// Train a RandomForest model.
// Empty categoricalFeaturesInfo indicates all features are continuous.
        Integer numClasses = 2;
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
        Integer numTrees = 3; // Use more in practice.
        String featureSubsetStrategy = "auto"; // Let the algorithm choose.
        String impurity = "gini";
        Integer maxDepth = 5;
        Integer maxBins = 32;
        Integer seed = 12345;

        RandomForestModel model = RandomForest.trainClassifier(trainingData, numClasses,
                categoricalFeaturesInfo, numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins,
                seed);

// Evaluate model on test instances and compute test error
        JavaPairRDD<Double, Double> predictionAndLabel =
                testData.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));
        double testErr =
                predictionAndLabel.filter(pl -> !pl._1().equals(pl._2())).count() / (double) testData.count();
        System.out.println("Test Error: " + testErr);
        System.out.println("Learned classification forest model:\n" + model.toDebugString());

// Save and load model
        model.save(jsc.sc(), "/Users/erickaudet/dev/trainingbot/test_data/myRandomForestClassificationModel");
        RandomForestModel sameModel = RandomForestModel.load(jsc.sc(),
                "/Users/erickaudet/dev/trainingbot/test_data/myRandomForestClassificationModel");
        return MLClasses.normal;
    }
}
