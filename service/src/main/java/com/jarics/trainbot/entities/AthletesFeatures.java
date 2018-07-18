package com.jarics.trainbot.entities;


public class AthletesFeatures {
    double TSB;
    double CTL;
    double ATL;
    double TSS;
    double s;
    double NP;
    double NGP;
    String Athlete;

    public static String toHeaderString() {
        return "ATHLETE, TSB, CTL, ATL, TSS, s, NP, NGP\r\n";
    }

    public AthletesFeatures() {
    }

    public double getTSB() {
        return TSB;
    }

    public void setTSB(double TSB) {
        this.TSB = TSB;
    }

    public double getCTL() {
        return CTL;
    }

    public void setCTL(double CTL) {
        this.CTL = CTL;
    }

    public double getATL() {
        return ATL;
    }

    public void setATL(double ATL) {
        this.ATL = ATL;
    }

    public double getTSS() {
        return TSS;
    }

    public void setTSS(double TSS) {
        this.TSS = TSS;
    }

    public double getS() {
        return s;
    }

    public void setS(double s) {
        this.s = s;
    }

    public double getNP() {
        return NP;
    }

    public void setNP(double NP) {
        this.NP = NP;
    }

    public double getNGP() {
        return NGP;
    }

    public void setNGP(double NGP) {
        this.NGP = NGP;
    }

    public String getAthlete() {
        return Athlete;
    }

    public void setAthlete(String athlete) {
        Athlete = athlete;
    }

    public String toCsvString() {
        return Athlete + ", " + TSB + ", " + CTL + ", " + ATL + ", " + TSS + ", " + s + ", " + NP + ", " + NGP + "\r\n";
    }
}
