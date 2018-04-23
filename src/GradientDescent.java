import java.util.*;

public class GradientDescent {

    private double[][] trainingData;
    private double[]   means;
    private double[]   scale;

    private double[]   parameters;
    private double     learningRate;

    GradientDescent() {
        this.learningRate = 0D;
    }

    public double predict(double[] inp) {
        return predict(inp, this.parameters);
    }
    private double predict(double[] inp, double[] parameters){
        double[] features = concatenate(new double[]{1}, inp);

        double prediction = 0;
        for(int j = 0; j < features.length; j++) {
            prediction += parameters[j] * features[j];
        }

        return prediction;
    }

    public void train(){
        readjustLearningRate();

        double costFunctionDelta = Math.abs(costFunction() - costFunction(iterateGradient()));

        while(costFunctionDelta > 0.0000000001) {
            System.out.println("Old cost function : " + costFunction());
            System.out.println("New cost function : " + costFunction(iterateGradient()));
            System.out.println("Delta: " + costFunctionDelta);

            parameters = iterateGradient();
            costFunctionDelta = Math.abs(costFunction() - costFunction(iterateGradient()));
            readjustLearningRate();
        }
    }

    private double[] iterateGradient() {
        double[] nextParameters = new double[parameters.length];
        // Calculate parameters for the next iteration
        for(int r = 0; r < parameters.length; r++) {
            nextParameters[r] = parameters[r] - learningRate * partialDerivative(r);
        }

        return nextParameters;
    }
    private double partialDerivative(int index) {
        double sum = 0;
        for(int i = 0; i < trainingData.length; i++) {
            int indexOfResult = trainingData[i].length - 1;
            double[] input = Arrays.copyOfRange(trainingData[i], 0, indexOfResult);
            sum += ((predict(input) - trainingData[i][indexOfResult]) * trainingData[i][index]);
        }

        return sum/trainingData.length ;
    }
    private void readjustLearningRate() {

        while(costFunction(iterateGradient()) > costFunction()) {
            // If the cost function of the new parameters is higher that the current cost function
            // it means the gradient is diverging and we have to adjust the learning rate
            // and recalculate new parameters
            System.out.print("Learning rate: " + learningRate + " is too big, readjusted to: ");
            learningRate = learningRate/2;
            System.out.println(learningRate);
        }
        // otherwise we are taking small enough steps, we have the right learning rate
    }

    public double[][] getTrainingData() {
        return trainingData;
    }
    public void setTrainingData(double[][] data) {
        this.trainingData = data;
        this.means = new double[this.trainingData[0].length-1];
        this.scale = new double[this.trainingData[0].length-1];

        for(int j = 0; j < data[0].length-1; j++) {
            double min = data[0][j], max = data[0][j];
            double sum = 0;
            for(int i = 0; i < data.length; i++) {
                if(data[i][j] < min) min = data[i][j];
                if(data[i][j] > max) max = data[i][j];
                sum += data[i][j];
            }
            scale[j] = max - min;
            means[j] = sum / data.length;
        }
    }

    public double[] getParameters() {
        return parameters;
    }
    public void setParameters(double[] parameters) {
        this.parameters = parameters;
    }

    public double getLearningRate() {
        return learningRate;
    }
    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    /**              1      m           i     i  2
     *   J(theta) = ----- * SUM( h     (x ) - y  )
     *               2*m    i=1   theta
     */
    public double costFunction() {
        return costFunction(this.parameters);
    }
    private double costFunction(double[] parameters) {
        int m = trainingData.length;
        double sum = 0;

        for(int i = 0; i < m; i++) {
            int indexOfResult = trainingData[i].length - 1;
            double[] input = Arrays.copyOfRange(trainingData[i], 0, indexOfResult);
            sum += Math.pow(predict(input, parameters) - trainingData[i][indexOfResult], 2);
        }

        double factor = 1D/(2*m);
        return factor * sum;
    }

    private double[] normalize(double[] input) {
        double[] normalized = new double[input.length];
        for(int i = 0; i < input.length; i++) {
            normalized[i] = (input[i] - means[i]) / scale[i];
        }

        return normalized;
    }

    private double[] concatenate(double[] a, double[] b) {
        int size = a.length + b.length;

        double[] concatArray = new double[size];
        int index = 0;

        for(double d : a) {
            concatArray[index++] = d;
        }
        for(double d : b) {
            concatArray[index++] = d;
        }

        return concatArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("hypothesis: ");
        int i = 0;
        sb.append(parameters[i++] + " + ");
        for(; i < parameters.length-1; i++) {
            sb.append(parameters[i] + "*x" + i + " + ");
        }
        sb.append(parameters[i] + "*x" + i);

        sb.append("\n Feature scale: ");
        for(i = 0; i < scale.length-1; i++) {
            sb.append(scale[i] + " ");
        }
        sb.append(scale[i]);

        sb.append("\n Feature means: ");
        for(i = 0; i < means.length-1; i++) {
            sb.append(means[i] + " ");
        }
        sb.append(means[i]);

        sb.append("\n Cost fuction: " + costFunction());

        return sb.toString();
    }

    public static void main(String[] args) {

        final double[][] TDATA = {
                //number of rooms, area, price
                {10, 2, 200, 200000},
                {3, 3, 300, 300000},
                {21, 14, 400, 400000},
                {34, 5, 500, 500000},
                {21, 800, 800000},
                {7, 9, 900, 900000}
        };

        GradientDescent gd = new GradientDescent();
        gd.setTrainingData(TDATA);
        gd.setParameters(new double[]{0D, 0D, 0D});
        gd.setLearningRate(0.1);
        gd.train();
        System.out.println(gd);
        System.out.println("PREDICTION: " + gd.predict(new double[]{3, 600}));
    }
}