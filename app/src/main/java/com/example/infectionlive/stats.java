package com.example.infectionlive;

public class stats implements Comparable{
    private String state;
    private int indian;
    private int foreign;
    private int recovered;
    private int deaths;

    public stats(String state, int indian, int foreign, int recovered, int deaths) {
        this.state = state;
        this.indian = indian;
        this.foreign = foreign;
        this.recovered = recovered;
        this.deaths = deaths;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getIndian() {
        return indian;
    }

    public void setIndian(int indian) {
        this.indian = indian;
    }

    public int getForeign() {
        return foreign;
    }

    public void setForeign(int foreign) {
        this.foreign = foreign;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    @Override
    public int compareTo(Object o) {
        int other = ((stats) o).getIndian()+((stats) o).getForeign();
        return other - (this.getIndian()+this.getForeign());
    }
}
