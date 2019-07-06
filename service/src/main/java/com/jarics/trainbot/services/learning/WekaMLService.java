package com.jarics.trainbot.services.learning;

import com.jarics.trainbot.com.jarics.trainbot.utils.FileUtils;
import com.jarics.trainbot.services.MLClasses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.evaluation.output.prediction.CSV;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class WekaMLService {

    @Value("${training.dataset.dir}")
    private String trainingDataSetDir;

    @Value("${training.dataset.filename}")
    private String trainingDataSetFileName;

    @Value("${raw.dataset.dir}")
    private String rawDataSetDir;

    @Value("${model.file.dir}")
    private String modelFileDir;

    @Value("${model.file.name}")
    private String modelFileName;

    @Value("${dataset.file.dir}")
    private String datasetFileDir;

    @Value("${dataset.filename}")
    private String datasetFileName;

    @Value("${learning.results.file.dir}")
    private String learningResultsFileDir;

    @Value("${learning.results.file.name}")
    private String learningResultsFileName;

    private GenerateTrainingDataset generateTrainingDataset;

    @Autowired
    public WekaMLService(
            GenerateTrainingDataset generateTrainingDataset) {
        this.generateTrainingDataset = generateTrainingDataset;
    }

    private Instances loadDataset() {
        Instances dataset = null;
        try {
            dataset = ConverterUtils.DataSource.read(trainingDataSetDir + trainingDataSetFileName);
            if (dataset.classIndex() == -1) {
                dataset.setClassIndex(dataset.numAttributes() - 1);
            }
        } catch (Exception ex) {
            Logger.getLogger(WekaMLService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataset;
    }

    public void generate() throws Exception {
        generateTrainingDataset.generate(true);
    }

    public void learn() {
        BayesNet bayesNet = new BayesNet();
        Evaluation eval;
        try {
            FileUtils.prepareDir(datasetFileDir);
            FileUtils.prepareDir(learningResultsFileDir);
            FileUtils.prepareDir(modelFileDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Instances instances = loadDataset();
        try {
            eval = new Evaluation(instances.trainCV(10, 0));
            bayesNet.setOptions(new String[]{"-D"});
            StringBuffer buffer = new StringBuffer();
            CSV csv = new CSV();
            csv.setBuffer(buffer);
            csv.setNumDecimals(8); // use 8 decimals instead of default 6
            csv.setOutputFile(new java.io.File(learningResultsFileDir + learningResultsFileName));
            eval.crossValidateModel(bayesNet, instances, 10, new Random(1), csv);
            System.out.println(eval.toSummaryString("\nResults\n\n", false));
            System.out.println(eval.toMatrixString("Training"));
            // output collected predictions
            System.out.println(buffer.toString());

            //Let's say we are happy with the results of the evaluations we need to build the model.
            bayesNet.buildClassifier(instances);

            // serialize model and dataset
            SerializationHelper.write(modelFileDir + modelFileName, bayesNet);
            SerializationHelper.write(datasetFileDir + datasetFileName, instances.lastInstance());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The BayesNet must have been trained and the method buildClassifier must have been called.
     *
     * @return
     */
    public MLClasses classify(double tsb, double ctl, double atl) throws Exception {
        double classification;
        Classifier classifier = (Classifier) SerializationHelper.read(modelFileDir + modelFileName);
        Instance instance = (Instance) SerializationHelper.read(datasetFileDir + datasetFileName);
        instance.setValue(0, tsb);
        instance.setValue(1, ctl);
        instance.setValue(2, atl);
        try {
            classification = classifier.classifyInstance(instance);
            if (classification == 0) {
                return MLClasses.normal;
            } else if (classification == 1) {
                return MLClasses.overtrained;
            } else if (classification == 2) {
                return MLClasses.undertrained;
            } else {
                return MLClasses.unknown;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return MLClasses.unknown;
    }
}
