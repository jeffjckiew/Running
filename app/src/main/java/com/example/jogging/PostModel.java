package com.example.jogging;

public class PostModel {
    private int id;
    private String date;
    private String breakfast;
    private String lunch;
    private String dinner;
    private String extra;

    public PostModel(){

    }
    public PostModel(String date,String breakfast, String lunch, String dinner, String extra){
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.extra = extra;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
