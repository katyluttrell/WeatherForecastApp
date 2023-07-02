package com.katy.weatherforecastapp.Network

import com.katy.weatherforecastapp.Responses.FiveDayForecastResponse
import com.katy.weatherforecastapp.Responses.LatLonResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {

    @GET("data/2.5/forecast")
    fun getFiveDayForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String): Call<FiveDayForecastResponse>

    @GET("/geo/1.0/zip")
    fun getLatLon(
        @Query("zip") zipCode: String,
        @Query("appid") apiKey: String): Call<LatLonResponse>
}