package com.example.weatherapplication20;

import com.google.gson.annotations.SerializedName;

public class rain {

    @SerializedName("3h")
    private float volume;

    public float getVolume() {
        return volume;
    }
}
