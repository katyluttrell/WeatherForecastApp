package com.katy.weatherforecastapp.repository

import com.katy.weatherforecastapp.model.WeatherData

interface WeatherRepository {
    suspend fun cacheFiveDayForecastList(forecast: List<List<WeatherData>>, zipcode:String)
    suspend fun getFiveDayForecastList(): List<List<WeatherData>>?
    suspend fun deleteAllWeatherData()
}