package com.katy.weatherforecastapp.repository

import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getFiveDayForecastListFlow(
        location: Location,
        errorCallbacks: WeatherDataErrorCallbacks
    ): Flow<List<List<WeatherData>>>

    suspend fun deleteAllWeatherData()
}