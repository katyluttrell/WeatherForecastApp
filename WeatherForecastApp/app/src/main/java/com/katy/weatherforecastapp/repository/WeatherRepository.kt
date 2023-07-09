package com.katy.weatherforecastapp.repository

import com.katy.weatherforecastapp.model.WeatherData

interface WeatherRepository {
    suspend fun cacheFiveDayForecastList(forecast: List<List<WeatherData>>)
    suspend fun getFiveDayForecastList(): List<List<WeatherData>>?
    suspend fun deleteAllWeatherData()
}