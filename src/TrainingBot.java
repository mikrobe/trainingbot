import java.util.ArrayList;
import java.util.List;

public class TrainingBot {

    public static void main(String args[]){
        TrainingBot wTrainingBot = new TrainingBot();
        List<Session> sessions = wTrainingBot.getSessionDuration(1, 24, 20);
        for (Session s:sessions){
            System.out.println(s.getTotalDuration());
        }
    }

    public List<Session> getSessionDuration(int pWeek, double pInitialDuration, int pNumberOfWeeks){
        double[] adjusters = {-0.41, 0.70, 0.10, 0.10};
        double lastShortSessionTime = pInitialDuration;
        double shortTrainingDuration = pInitialDuration;
        double longTrainingDuration = 0;
        double longSession = 0.67;
        List<Session> wSessions = null;

        for (int tw = 1; tw < pNumberOfWeeks; tw++) {
            WeekFocus weekFocus = getWeekFocus(tw, pNumberOfWeeks);
            switch (weekFocus) {
                case regular:
                    shortTrainingDuration = lastShortSessionTime + adjusters[Math.floorMod(tw, 4)] * lastShortSessionTime;
                    break;
                case repeeting: {
                    wSessions = getSessionDuration(tw - 4, pInitialDuration, pNumberOfWeeks);
                    break;
                }
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
                if (wSessions == null) {
                    wSessions = new ArrayList<Session>();
                    Set wSet = new Set(shortTrainingDuration, 0);
                    Session wShortSession = new Session();
                    wShortSession.addSet(new Set(shortTrainingDuration, 0));
                    Session wLongSession = new Session();
                    wLongSession.addSet(new Set(longTrainingDuration, 0));
                    wSessions.add(wShortSession);
                    wSessions.add(wLongSession);
                }
                return wSessions;
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
