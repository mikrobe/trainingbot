import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Session {
    boolean overTrainingIndicator = false;
    Date date;
    List<Set> sets;

    public Session() {
        sets = new ArrayList<Set>();
    }

    public List<Set> getSets() {
        return sets;
    }

    public void addSet(Set set) {
        this.sets.add(set);
    }

    public double getTotalDuration() {
        double wTotalDuration = 0;
        for (Set set:sets) {
            wTotalDuration = set.getDuration();
        }
        return wTotalDuration;
    }

    public boolean isOverTrainingIndicator() {
        return overTrainingIndicator;
    }

    public void setOverTrainingIndicator(boolean overTrainingIndicator) {
        this.overTrainingIndicator = overTrainingIndicator;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
