import java.util.ArrayList;
import java.util.List;

public class Session {
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
}
