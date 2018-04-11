public class Set {
    public Set(double work, double rest) {
        this.work = new Effort(work);
        this.rest = new Effort(rest);
    }
    private Effort work;
    private Effort rest;
    public double getDuration(){
        return work.getDuration() + rest.getDuration();
    }
}
