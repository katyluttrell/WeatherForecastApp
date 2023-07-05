package com.katy.weatherforecastapp.repository

import com.katy.weatherforecastapp.database.dao.LocationDao
import com.katy.weatherforecastapp.database.dao.WeatherDataDao
import com.katy.weatherforecastapp.model.Location
import com.katy.weatherforecastapp.model.WeatherData
import com.katy.weatherforecastapp.util.Utils

class WeatherRepositoryImpl (private val locationDao: LocationDao, private val weatherDataDao: WeatherDataDao): WeatherRepository{
    override suspend fun addFiveDayForecastList(forecast: List<List<WeatherData>>) {
       forecast.flatten().forEach { weatherDataDao.addWeatherData(it) }
    }

    override suspend fun getFiveDayForecastList(): List<List<WeatherData>>? {
        return weatherDataDao.getWeatherData()?.let { Utils.organizeWeatherDataByDay(it) }
    }

    override suspend fun addLocation(location: Location) = locationDao.addLocation(location)

    override suspend fun getLocation(): Location? = locationDao.getLocation()

}