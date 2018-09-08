package com.jarics.trainbot.services.learning;

import com.sun.xml.internal.ws.api.policy.ModelGenerator;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
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
            Logger.getLogger(ModelGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataset;
    }

    private void learn(Instances instances) {
        BayesNet bayesNet = new BayesNet();
        Evaluation eval;
        try {
            eval = new Evaluation(instances);
            bayesNet.setOptions(new String[]{"-D"});
            eval.crossValidateModel(bayesNet, instances, 10, new Random(1));
            System.out.println(eval.toSummaryString("\nResults\n\n", false));
            System.out.println(eval.toMatrixString("Training"));
//            bayesNet.buildClassifier(instances);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
