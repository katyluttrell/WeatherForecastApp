package com.katy.weatherforecastapp.repository

import com.katy.weatherforecastapp.model.Location

interface WeatherRepository {

    suspend fun addLocation(location: Location)
    suspend fun getLocation(): Location?
}