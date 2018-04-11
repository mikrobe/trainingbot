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
        double lastShortSessionTime = pInitialDuration;
        double shortTrainingDuration = pInitialDuration;
        double longTrainingDuration = 0;
        double longSession = 0.67;

        for (int tw = 1; tw < pNumberOfWeeks; tw++) {
            WeekFocus weekFocus = getWeekFocus(tw, pNumberOfWeeks);
            switch (weekFocus) {
                case regular:
                    shortTrainingDuration = lastShortSessionTime + adjusters[Math.floorMod(tw, 4)] * lastShortSessionTime;
                    break;
                case repeeting:
                    shortTrainingDuration = getSessionDuration(tw-4, pInitialDuration, pNumberOfWeeks)[0];
                    break;
                case breakWeek:
                    shortTrainingDuration = lastShortSessionTime;
                    break;
                case firstWeek:
                    shortTrainingDuration = lastShortSessionTime;
                    break;
            }
            longTrainingDuration = shortTrainingDuration + longSession * shortTrainingDuration;
            lastShortSessionTime = shortTrainingDuration;
            if (tw == pWeek) {
                //TODO return Session object
                return new double[]{shortTrainingDuration, longTrainingDuration};
            }
        }
        return null;
    }

    private static WeekFocus getWeekFocus(int tw, int pNumberOfWeeks) {
        double[] breakFocus = {0.74,0,76};
        double[] repeatingFocus = {0.84, 0.86};
        double r = tw/pNumberOfWeeks;
        if (tw == 1)
            return WeekFocus.firstWeek;
        if (r >= breakFocus[0] && r <= breakFocus[1])
            return WeekFocus.breakWeek;
        if (r >= repeatingFocus[0] && r <= repeatingFocus[1])
            return WeekFocus.repeeting;
        return WeekFocus.regular;
    }
}
