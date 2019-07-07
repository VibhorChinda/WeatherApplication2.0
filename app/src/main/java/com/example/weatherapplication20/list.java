package com.example.weatherapplication20;

import java.util.ArrayList;

public class list {

    private long dt;
    private main main;
    private ArrayList<weather> weather;
    private clouds clouds;
    private wind wind;
    private rain rain;
    private sys sys;
    private String dt_txt;

    public long getDt() {
        return dt;
    }

    public com.example.weatherapplication20.main getMain() {
        return main;
    }

    public ArrayList<com.example.weatherapplication20.weather> getWeather() {
        return weather;
    }

    public com.example.weatherapplication20.clouds getClouds() {
        return clouds;
    }

    public com.example.weatherapplication20.wind getWind() {
        return wind;
    }

    public com.example.weatherapplication20.rain getRain() {
        return rain;
    }

    public com.example.weatherapplication20.sys getSys() {
        return sys;
    }

    public String getDt_txt() {
        return dt_txt;
    }
}
