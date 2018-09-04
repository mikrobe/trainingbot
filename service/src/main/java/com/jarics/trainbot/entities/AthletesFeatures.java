package com.jarics.trainbot.entities;


public class AthletesFeatures {
    double tSB;
    double cTL;
    double aTL;
    double tSS;
    double s;
    double nP;
    double nGP;
    AthleteFTP athlete;

    public double gettSB() {
        return tSB;
    }

    public void settSB(double tSB) {
        this.tSB = tSB;
    }

    public double getcTL() {
        return cTL;
    }

    public void setcTL(double cTL) {
        this.cTL = cTL;
    }

    public double getaTL() {
        return aTL;
    }

    public void setaTL(double aTL) {
        this.aTL = aTL;
    }

    public double gettSS() {
        return tSS;
    }

    public void settSS(double tSS) {
        this.tSS = tSS;
    }

    public double getS() {
        return s;
    }

    public void setS(double s) {
        this.s = s;
    }

    public double getnP() {
        return nP;
    }

    public void setnP(double nP) {
        this.nP = nP;
    }

    public double getnGP() {
        return nGP;
    }

    public void setnGP(double nGP) {
        this.nGP = nGP;
    }

    public AthleteFTP getAthlete() {
        return athlete;
    }

    public void setAthlete(AthleteFTP athlete) {
        this.athlete = athlete;
    }

    public AthletesFeatures() {
    }

    /*
     *
     * libsvm-format
     * <class/target>[ <attribute number>:<attribute value>]*
     *
     * Sample:
     * 1 1:-0.222222 2:0.5 3:-0.762712 4:-0.833333
     * 1 1:-0.555556 2:0.25 3:-0.864407 4:-0.916667
     * 1 1:-0.722222 2:-0.166667 3:-0.864407 4:-0.833333
     */
    public String toLibSvm() {
        StringBuilder ret = new StringBuilder();
        switch (athlete.getClassification()) {
            case normal: {
                ret.append("0 ");
                break;
            }
            case overtrained: {
                ret.append("1 ");
                break;
            }
            case undertrained: {
                ret.append("2 ");
                break;
            }
        }
        ret.append("1:" + tSB);
        ret.append(" 2:" + cTL);
        ret.append(" 3:" + aTL);
        ret.append(" 4:" + tSS);
        ret.append(" 5:" + s);
        ret.append(" 6:" + nP);
        ret.append(" 7:" + nGP);
        ret.append("\r\n");
        return ret.toString();
    }

    /**
     * sunny, 85, 85, FALSE, no
     * sunny,80,90,TRUE,no
     * overcast,83,86,FALSE,yes
     * rainy,70,96,FALSE,yes
     * rainy,68,80,FALSE,yes
     * rainy,65,70,TRUE,no
     * overcast,64,65,TRUE,yes
     * sunny,72,95,FALSE,no
     * sunny,69,70,FALSE,yes
     * rainy,75,80,FALSE,yes
     * sunny,75,70,TRUE,yes
     * overcast,72,90,TRUE,yes
     * overcast,81,75,FALSE,yes
     * rainy,71,91,TRUE,no
     */
    public String toArffData() {
        StringBuilder ret = new StringBuilder();
        ret.append(tSB + "," + cTL + "," + aTL);
        switch (athlete.getClassification()) {
            case normal: {
                ret.append(",normal");
                break;
            }
            case overtrained: {
                ret.append(",overtrained");
                break;
            }
            case undertrained: {
                ret.append(",undertrained");
                break;
            }
        }
        ret.append("\n");
        return ret.toString();

    }

    /**
     * * @relation weather
     * *
     * * @attribute outlook {sunny, overcast, rainy}
     * * @attribute temperature real
     * * @attribute humidity real
     * * @attribute windy {TRUE, FALSE}
     * * @attribute play {yes, no}
     * *
     * * @data
     */
    public static String toArffHeader() {
        StringBuilder ret = new StringBuilder();
        ret.append("@relation training\n");
        ret.append("@attribute tsb real\n");
        ret.append("@attribute ctl real\n");
        ret.append("@attribute atl real\n");
        ret.append("@attribute class {normal, overtrained, undertrained}\n");
        ret.append("@data\n");
        return ret.toString();
    }

}
