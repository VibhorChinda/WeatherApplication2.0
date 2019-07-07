package com.example.weatherapplication20;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface api_interface {

    @GET("forecast")
    Call<ModalClass> GetWeatherConditions(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String api_key);
}
