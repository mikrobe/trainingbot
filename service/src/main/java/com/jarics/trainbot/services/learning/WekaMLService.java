package com.jarics.trainbot.services.learning;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.evaluation.output.prediction.CSV;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WekaMLService {

    public static void main(String[] args) {
        String trainingDataSetPath = args[0];
        WekaMLService wekaMLService = new WekaMLService();
        Instances instances = wekaMLService.loadDataset(trainingDataSetPath);
        wekaMLService.learn(instances.trainCV(10, 0));
    }

    private Instances loadDataset(String path) {
        Instances dataset = null;
        try {
            dataset = ConverterUtils.DataSource.read(path);
            if (dataset.classIndex() == -1) {
                dataset.setClassIndex(dataset.numAttributes() - 1);
            }
        } catch (Exception ex) {
            Logger.getLogger(WekaMLService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataset;
    }

    private void learn(Instances instances) {
        BayesNet bayesNet = new BayesNet();
        Evaluation eval;
        try {
            eval = new Evaluation(instances);
            bayesNet.setOptions(new String[]{"-D"});
            StringBuffer buffer = new StringBuffer();
            CSV csv = new CSV();
            csv.setBuffer(buffer);
            csv.setNumDecimals(8); // use 8 decimals instead of default 6
            csv.setOutputFile(new java.io.File("predictions.csv"));
            eval.crossValidateModel(bayesNet, instances, 10, new Random(1), csv);
            System.out.println(eval.toSummaryString("\nResults\n\n", false));
            System.out.println(eval.toMatrixString("Training"));
            // output collected predictions
            System.out.println(buffer.toString());

//            bayesNet.buildClassifier(instances);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
