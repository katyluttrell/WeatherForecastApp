package com.katy.weatherforecastapp.network

import com.katy.weatherforecastapp.model.FiveDayForecast
import com.katy.weatherforecastapp.model.LatLonResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {

    @GET("data/2.5/forecast")
    fun getFiveDayForecast(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String): Call<FiveDayForecast>

    @GET("/geo/1.0/zip")
    fun getLatLon(
        @Query("zip") zipCode: String,
        @Query("appid") apiKey: String): Call<LatLonResponse>
}