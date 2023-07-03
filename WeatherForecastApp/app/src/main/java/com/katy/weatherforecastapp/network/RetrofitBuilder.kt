package com.katy.weatherforecastapp.network

import com.katy.weatherforecastapp.adapters.DateAdapter
import com.katy.weatherforecastapp.adapters.WeatherAdapter
import com.katy.weatherforecastapp.adapters.WeatherDataListAdapter
import com.katy.weatherforecastapp.model.Weather
import com.katy.weatherforecastapp.model.WeatherData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://api.openweathermap.org/"
fun buildClient(): OkHttpClient =
    OkHttpClient.Builder()
        .build()

fun buildRetrofit(): Retrofit {
    return Retrofit.Builder()
        .client(buildClient())
        .baseUrl(BASE_URL)
        .addConverterFactory(
            MoshiConverterFactory.create(Moshi.Builder()
                .add(WeatherAdapter())
                .add(DateAdapter())
                .add(WeatherDataListAdapter()).build()))
        .build()
}

fun buildApiService(): OpenWeatherApiService =
    buildRetrofit().create(OpenWeatherApiService::class.java)