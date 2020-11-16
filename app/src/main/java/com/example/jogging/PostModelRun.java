package com.example.jogging;

public class PostModelRun {
    private int id;
    private String date;
    private String runtime;
    private String distance;
    private String pace;

    public PostModelRun(){

    }

    public PostModelRun(String date,String runtime, String distance, String pace){
        this.runtime = runtime;
        this.distance = distance;
        this.pace = pace;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPace() {
        return pace;
    }

    public void setPace(String pace) {
        this.pace = pace;
    }
}
