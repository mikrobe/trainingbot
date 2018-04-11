public class Main {

    public static void main(String[] args) {
        System.out.println("TrainBot");
        for (int i = 1; i < 20; i++) {
            double[] training = getSessionDuration(i, 24, 20);
            System.out.println(training[0]);
            System.out.println(training[1]);
        }
    }

    static public double[] getSessionDuration(int pWeek, double pInitialDuration, int pNumberOfWeeks){
        double[] adjusters = {-0.41, 0.70, 0.10, 0.10};
        int trainingWeeks = pNumberOfWeeks;
        double lastShortSessionTime = pInitialDuration;
        double shortTrainingDuration = 0;
        double longTrainingDuration = 0;
        double longSession = 0.67;

        for (int tw = 1; tw < trainingWeeks; tw++) {
            switch (tw) {
                case 15:
                    shortTrainingDuration = lastShortSessionTime;
                    break;
                case 1:
                    shortTrainingDuration = lastShortSessionTime;
                    break;
                case 17:
                    shortTrainingDuration = getSessionDuration(tw-4, pInitialDuration, pNumberOfWeeks)[0];
                    break;
                default:
                    shortTrainingDuration = lastShortSessionTime + adjusters[Math.floorMod(tw, 4)] * lastShortSessionTime;
                    break;
            }
            longTrainingDuration = shortTrainingDuration + longSession * shortTrainingDuration;
            lastShortSessionTime = shortTrainingDuration;
            if (tw == pWeek) {
                return new double[]{shortTrainingDuration, longTrainingDuration};
            }
        }
        return null;
    }
}
