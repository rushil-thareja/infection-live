package com.example.infectionlive;

public class beds {
    String state;
    int rural_hospitals;
    int rural_beds;
    int urban_hospitals;
    int urban_beds;
    int total_hospitals;
    int total_beds;

    public beds(String state, int rural_hospitals, int rural_beds, int urban_hospitals, int urban_beds, int total_hospitals, int total_beds) {
        this.state = state;
        this.rural_hospitals = rural_hospitals;
        this.rural_beds = rural_beds;
        this.urban_hospitals = urban_hospitals;
        this.urban_beds = urban_beds;
        this.total_hospitals = total_hospitals;
        this.total_beds = total_beds;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getRural_hospitals() {
        return rural_hospitals;
    }

    public void setRural_hospitals(int rural_hospitals) {
        this.rural_hospitals = rural_hospitals;
    }

    public int getRural_beds() {
        return rural_beds;
    }

    public void setRural_beds(int rural_beds) {
        this.rural_beds = rural_beds;
    }

    public int getUrban_hospitals() {
        return urban_hospitals;
    }

    public void setUrban_hospitals(int urban_hospitals) {
        this.urban_hospitals = urban_hospitals;
    }

    public int getUrban_beds() {
        return urban_beds;
    }

    public void setUrban_beds(int urban_beds) {
        this.urban_beds = urban_beds;
    }

    public int getTotal_hospitals() {
        return total_hospitals;
    }

    public void setTotal_hospitals(int total_hospitals) {
        this.total_hospitals = total_hospitals;
    }

    public int getTotal_beds() {
        return total_beds;
    }

    public void setTotal_beds(int total_beds) {
        this.total_beds = total_beds;
    }
}
