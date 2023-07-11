package com.katy.weatherforecastapp.network

import com.katy.weatherforecastapp.model.remote.NetworkFiveDayForecast
import com.katy.weatherforecastapp.model.remote.NetworkLocation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {
    @GET("data/2.5/forecast")
    suspend fun getFiveDayForecast(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): Response<NetworkFiveDayForecast>

    @GET("/geo/1.0/zip")
    suspend fun getLatLon(
        @Query("zip") zipCode: String, @Query("appid") apiKey: String
    ): Response<NetworkLocation>
}