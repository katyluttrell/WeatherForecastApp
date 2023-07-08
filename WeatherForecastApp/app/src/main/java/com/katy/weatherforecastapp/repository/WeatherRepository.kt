package com.katy.weatherforecastapp.repository

import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData

interface WeatherRepository {
    suspend fun addFiveDayForecastList(forecast: List<List<WeatherData>>)
    suspend fun getFiveDayForecastList(): List<List<WeatherData>>?
    suspend fun deleteAllWeatherData()
}